package com.xsofty.filter.data.datasource.remote

import com.xsofty.filter.domain.model.HelloWorldEntity
import retrofit2.Response
import retrofit2.http.GET

interface HelloWorldApi {

    @GET("hello/world")
    suspend fun getHelloWorld(): Response<HelloWorldEntity>
}