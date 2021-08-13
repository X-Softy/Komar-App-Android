package com.xsofty.categories.data.repository

import com.xsofty.categories.data.network.api.CategoriesApi
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.categories.domain.repository.CategoriesRepository
import com.xsofty.shared.Result
import com.xsofty.shared.di.CoroutinesModule.IoDispatcher
import com.xsofty.shared.network.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class CategoriesRepositoryImpl @Inject constructor(
    private val api: CategoriesApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CategoriesRepository {

    override suspend fun getCategories(): Result<List<CategoryEntity>> {
        return safeApiCall(dispatcher) {
            api.getCategories().map { it.toCategoryEntity() }
        }
    }
}