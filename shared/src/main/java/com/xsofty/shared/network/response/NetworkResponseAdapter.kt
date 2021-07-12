package com.xsofty.shared.network.response

import com.xsofty.shared.Result
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class NetworkResponseAdapter<T : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, T>
) : CallAdapter<T, Call<Result<T>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<Result<T>> {
        return NetworkResponseCall(call, errorBodyConverter)
    }
}