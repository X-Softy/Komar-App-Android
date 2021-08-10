package com.xsofty.categories.domain.repository

import com.xsofty.categories.domain.model.CategoryEntity
import com.xsofty.shared.Result

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<CategoryEntity>>
}