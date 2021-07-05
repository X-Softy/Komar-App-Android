package com.xsofty.filter.domain.usecase

import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.filter.domain.repository.HelloWorldRepository
import com.xsofty.shared.di.CoroutinesModule
import com.xsofty.shared.domain.UseCase
import com.xsofty.shared.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class HelloWorldUseCase @Inject constructor(
    private val repository: HelloWorldRepository,
    @CoroutinesModule.IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : UseCase<Unit, HelloWorldEntity?>(coroutineDispatcher) {

    override suspend fun execute(parameters: Unit): HelloWorldEntity? {
        return repository.getHelloWorld()
    }
}