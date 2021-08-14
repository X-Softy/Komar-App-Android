package com.xsofty.rooms.data.network.model

import com.squareup.moshi.Json
import com.xsofty.rooms.domain.model.entity.CommentEntity

data class CommentDto(

    @field:Json(name = "userId")
    val userId: String,

    @field:Json(name = "comment")
    val text: String
) {
    fun toCommentEntity(): CommentEntity {
        return CommentEntity(
            userId, text
        )
    }
}