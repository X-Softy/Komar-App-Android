package com.xsofty.rooms.data.network.model

import com.squareup.moshi.Json
import com.xsofty.rooms.domain.model.entity.RoomEntity

data class RoomDto(

    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "description")
    val description: String
) {
    fun toRoomEntity(): RoomEntity {
        return RoomEntity(
            id, title, description
        )
    }
}
