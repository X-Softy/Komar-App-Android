package com.xsofty.rooms.domain.repository

import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.domain.model.params.CreateRoomParams
import com.xsofty.shared.Result

interface RoomsRepository {
    suspend fun getRoomsByCategory(categoryId: String): Result<List<RoomEntity>>
    suspend fun getMyRooms(): Result<List<RoomEntity>>
    suspend fun createRoom(params: CreateRoomParams): Result<Unit>
}