package com.xsofty.categories.data.network.api

import com.xsofty.categories.data.network.model.CategoryDto
import retrofit2.http.GET

interface CategoriesApi {

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}