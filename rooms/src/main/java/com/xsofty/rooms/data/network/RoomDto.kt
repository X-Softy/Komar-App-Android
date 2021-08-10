package com.xsofty.rooms.data.network

import com.squareup.moshi.Json
import com.xsofty.rooms.domain.model.RoomEntity

data class RoomDto(

    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "title")
    val title: String
) {
    fun toRoomEntity(): RoomEntity {
        return RoomEntity(
            id = id,
            title = title
        )
    }
}