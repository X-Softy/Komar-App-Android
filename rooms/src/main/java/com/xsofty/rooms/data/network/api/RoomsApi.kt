package com.xsofty.rooms.data.network.api

import com.xsofty.rooms.data.network.model.RoomDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomsApi {

    @GET("rooms/category/{categoryId}")
    suspend fun getRoomsByCategory(@Path("categoryId") categoryId: String): List<RoomDto>
}