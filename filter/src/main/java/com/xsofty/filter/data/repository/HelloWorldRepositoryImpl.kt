package com.xsofty.filter.data.repository

import com.xsofty.filter.data.datasource.remote.HelloWorldRemoteDataSource
import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.filter.domain.repository.HelloWorldRepository
import com.xsofty.shared.Result
import javax.inject.Inject

internal class HelloWorldRepositoryImpl @Inject constructor(
    private val helloWorldRemoteDataSource: HelloWorldRemoteDataSource
): HelloWorldRepository {

    override suspend fun getHelloWorld(): Result<HelloWorldEntity> {
        return helloWorldRemoteDataSource.getHelloWorld()
    }
}