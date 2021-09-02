package com.xsofty.categories.data.network.model

import com.squareup.moshi.Json
import com.xsofty.categories.domain.model.entity.CategoryEntity

data class CategoryDto(

    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "imageId")
    var imageId: String
) {
    fun toCategoryEntity(): CategoryEntity {
        return CategoryEntity(
            id, title, imageId
        )
    }
}
