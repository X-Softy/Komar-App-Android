package com.xsofty.filter.data.datasource.remote

import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.shared.Result
import javax.inject.Inject

internal interface HelloWorldRemoteDataSource {
    suspend fun getHelloWorld(): Result<HelloWorldEntity>
}

internal class HelloWorldRemoteDataSourceImpl @Inject constructor(
    private val api: HelloWorldApi
): HelloWorldRemoteDataSource {

    override suspend fun getHelloWorld(): Result<HelloWorldEntity> {
        return api.getHelloWorld()
    }
}