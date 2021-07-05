package com.xsofty.filter.domain.repository

import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.shared.model.Result

interface HelloWorldRepository {
    suspend fun getHelloWorld(): HelloWorldEntity?
}