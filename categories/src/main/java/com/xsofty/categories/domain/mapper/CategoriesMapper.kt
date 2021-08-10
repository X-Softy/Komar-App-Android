package com.xsofty.categories.domain.mapper

import com.xsofty.categories.data.network.CategoryDto
import com.xsofty.categories.domain.model.CategoryEntity
import com.xsofty.shared.Result

class CategoriesMapper {

    fun mapToCategoryEntities(dto: Result<List<CategoryDto>>): Result<List<CategoryEntity>> {
        return when (dto) {
            is Result.Success -> {
                Result.Success(
                    dto.data.map { it.toCategoryEntity() }
                )
            }
            is Result.Error -> dto
            Result.Loading -> Result.Loading
        }
    }
}