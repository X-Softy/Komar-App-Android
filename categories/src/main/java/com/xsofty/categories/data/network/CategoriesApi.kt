package com.xsofty.categories.data.network

import retrofit2.http.GET

interface CategoriesApi {

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}