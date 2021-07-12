package com.xsofty.filter.domain.repository

import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.shared.Result

interface HelloWorldRepository {
    suspend fun getHelloWorld(): Result<HelloWorldEntity>
}