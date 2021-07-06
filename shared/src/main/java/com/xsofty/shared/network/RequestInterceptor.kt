package com.xsofty.shared.network

import com.xsofty.shared.storage.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val appPreferences: AppPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(name = "Authorization", value = "Bearer ${appPreferences.idToken}")
            .build()

        return chain.proceed(request)
    }
}