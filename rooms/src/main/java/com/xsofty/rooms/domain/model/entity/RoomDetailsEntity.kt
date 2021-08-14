package com.xsofty.rooms.domain.model.entity

data class RoomDetailsEntity(
    val id: String,
    val title: String,
    val description: String,
    val categoryId: String,
    val creatorId: String,
    val comments: List<CommentEntity>,
    val joinedUserIds: List<String>
)
