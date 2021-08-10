package com.xsofty.rooms.data.network

import com.xsofty.rooms.data.network.RoomDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomsApi {

    @GET("rooms/category/{categoryId}")
    suspend fun getRooms(@Path("categoryId") categoryId: String): List<RoomDto>
}