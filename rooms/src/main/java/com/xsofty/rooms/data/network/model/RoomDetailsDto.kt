package com.xsofty.rooms.data.network.model

import com.squareup.moshi.Json
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity

data class RoomDetailsDto(

    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "description")
    val description: String,

    @field:Json(name = "categoryId")
    val categoryId: String,

    @field:Json(name = "creatorUserId")
    val creatorId: String,

    @field:Json(name = "comments")
    val comments: List<CommentDto>,

    @field:Json(name = "joinedUserIds")
    val joinedUserIds: List<String>
) {
    fun toRoomDetailsEntity(): RoomDetailsEntity {
        return RoomDetailsEntity(
            id,
            title,
            description,
            categoryId,
            creatorId,
            comments.map { it.toCommentEntity() },
            joinedUserIds
        )
    }
}