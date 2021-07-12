package com.xsofty.shared.network.response

import com.xsofty.shared.ApiError
import com.xsofty.shared.Result
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

internal class NetworkResponseCall<T>(
    private val delegate: Call<T>,
    private val errorConverter: Converter<ResponseBody, T>
) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {

        return delegate.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Result.Success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Result.Error(apiError = UNKNOWN_ERROR))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Result.Error(apiError = ApiError(code, errorBody.toString())))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Result.Error(apiError = UNKNOWN_ERROR))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val networkResponse = Result.Error(exception = throwable)
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun request(): Request = delegate.request()

    override fun execute(): Response<Result<T>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    companion object {
        private val UNKNOWN_ERROR = ApiError(code = 0, message = "Unknown Error")
    }
}