package com.xsofty.shared.network

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {

    //TODO: Add id token
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().build()
        return chain.proceed(request)
    }
}