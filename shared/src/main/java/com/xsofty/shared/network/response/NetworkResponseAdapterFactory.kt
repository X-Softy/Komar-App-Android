package com.xsofty.shared.network.response

import com.xsofty.shared.Result
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<Result<<Foo>> or Call<Result<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not Result then we can't handle this type, so we return null
        if (getRawType(responseType) != Result::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as Result<Foo> or Result<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyConverter = retrofit.nextResponseBodyConverter<Any>(
            null, successBodyType, annotations
        )

        return NetworkResponseAdapter(successBodyType, errorBodyConverter)
    }
}