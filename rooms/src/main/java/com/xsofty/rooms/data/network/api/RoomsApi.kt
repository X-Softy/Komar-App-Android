package com.xsofty.rooms.data.network.api

import com.xsofty.rooms.data.network.model.RoomDto
import com.xsofty.rooms.domain.model.params.CreateRoomParams
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomsApi {

    @GET("rooms/category/{categoryId}")
    suspend fun getRoomsByCategory(@Path("categoryId") categoryId: String): List<RoomDto>

    @GET("rooms/user")
    suspend fun getMyRooms(): List<RoomDto>

    @POST("rooms")
    suspend fun createRoom(@Body params: CreateRoomParams)
}