package com.xsofty.rooms.domain.model.entity

import com.xsofty.categories.domain.model.entity.CategoryEntity

data class RoomDetailsEntity(
    val id: String,
    val title: String,
    val description: String,
    val category: CategoryEntity,
    val roomStatus: RoomStatus
)

enum class RoomStatus {
    CREATOR, JOINED, NOT_JOINED, INACTIVE
}
