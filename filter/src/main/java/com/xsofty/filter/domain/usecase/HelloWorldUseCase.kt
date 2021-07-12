package com.xsofty.filter.domain.usecase

import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.filter.domain.repository.HelloWorldRepository
import com.xsofty.shared.Result
import com.xsofty.shared.di.CoroutinesModule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class HelloWorldUseCase @Inject constructor(
    private val repository: HelloWorldRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<HelloWorldEntity> {
        return try {
            withContext(dispatcher) {
                repository.getHelloWorld()
            }
        } catch (e: Exception) {
            Timber.d(e)
            Result.Error(e)
        }
    }
}