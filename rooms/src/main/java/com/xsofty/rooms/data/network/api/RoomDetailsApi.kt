package com.xsofty.rooms.data.network.api

import com.xsofty.rooms.data.network.model.RoomDetailsDto
import com.xsofty.rooms.domain.model.params.AddCommentParams
import retrofit2.http.*

interface RoomDetailsApi {

    @GET("rooms/details/{roomId}")
    suspend fun getRoomDetails(@Path("roomId") roomId: String): RoomDetailsDto

    @PATCH("rooms/comment/{roomId}")
    suspend fun addComment(@Path("roomId") roomId: String, @Body params: AddCommentParams)

    @DELETE("rooms/{roomId}")
    suspend fun deleteRoom(@Path("roomId") roomId: String)

    @PATCH("rooms/join/{roomId}")
    suspend fun joinRoom(@Path("roomId") roomId: String)

    @PATCH("rooms/unjoin/{roomId}")
    suspend fun unjoinRoom(@Path("roomId") roomId: String)
}