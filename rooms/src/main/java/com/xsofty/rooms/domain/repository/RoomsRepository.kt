package com.xsofty.rooms.domain.repository

import com.xsofty.rooms.domain.model.RoomEntity
import com.xsofty.shared.Result

interface RoomsRepository {
    suspend fun getRoomsByCategory(categoryId: String): Result<List<RoomEntity>>
}