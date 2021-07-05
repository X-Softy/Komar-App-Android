package com.xsofty.filter.data.datasource.remote

import com.xsofty.filter.domain.model.HelloWorldEntity
import javax.inject.Inject

internal interface HelloWorldRemoteDataSource {
    suspend fun getHelloWorld(): HelloWorldEntity?
}

internal class HelloWorldRemoteDataSourceImpl @Inject constructor(
    private val api: HelloWorldApi
): HelloWorldRemoteDataSource {

    override suspend fun getHelloWorld(): HelloWorldEntity? {
        val response = api.getHelloWorld()
        return if (response.isSuccessful) {
            response.body()
        } else {
            HelloWorldEntity("ERROR")
        }
    }
}