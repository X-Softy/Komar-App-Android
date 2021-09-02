package com.xsofty.categories.data.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.xsofty.categories.data.network.api.CategoriesApi
import com.xsofty.categories.data.network.model.CategoryDto
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
            api.getCategories().map {
                it.toCategoryEntity()
            }
        }
    }
}