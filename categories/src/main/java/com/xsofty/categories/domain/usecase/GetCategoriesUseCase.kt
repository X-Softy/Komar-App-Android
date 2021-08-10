package com.xsofty.categories.domain.usecase

import com.xsofty.categories.domain.model.CategoryEntity
import com.xsofty.categories.domain.repository.CategoriesRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend operator fun invoke(): Result<List<CategoryEntity>> {
        return repository.getCategories()
    }
}