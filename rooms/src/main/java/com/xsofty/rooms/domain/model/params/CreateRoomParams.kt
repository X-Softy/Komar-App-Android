package com.xsofty.rooms.domain.model.params

data class CreateRoomParams(
    val categoryId: String,
    val title: String,
    val description: String
)
