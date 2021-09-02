package com.xsofty.rooms.domain.model.entity

import com.xsofty.categories.domain.model.entity.CategoryEntity

data class RoomDetailsEntity(
    val id: String,
    val title: String,
    val description: String,
    val category: CategoryEntity,
    val userStatus: UserStatus
)

enum class UserStatus {
    CREATOR, JOINED, NOT_JOINED
}
