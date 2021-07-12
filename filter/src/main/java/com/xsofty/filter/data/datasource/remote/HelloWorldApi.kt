package com.xsofty.filter.data.datasource.remote

import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.shared.Result
import retrofit2.http.GET

interface HelloWorldApi {

    @GET("hello/world")
    suspend fun getHelloWorld(): Result<HelloWorldEntity>
}