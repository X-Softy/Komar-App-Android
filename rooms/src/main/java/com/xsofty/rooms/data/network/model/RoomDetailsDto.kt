package com.xsofty.rooms.data.network.model

import com.squareup.moshi.Json
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.model.entity.UserStatus

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
    fun toRoomDetailsEntity(category: CategoryEntity, userId: String): RoomDetailsEntity {
        return RoomDetailsEntity(
            id,
            title,
            description,
            category,
            when {
                userId == creatorId -> UserStatus.CREATOR
                joinedUserIds.contains(userId) -> UserStatus.JOINED
                else -> UserStatus.NOT_JOINED
            }
        )
    }
}