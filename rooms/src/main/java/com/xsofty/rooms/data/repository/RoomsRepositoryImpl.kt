package com.xsofty.rooms.data.repository

import com.xsofty.rooms.data.network.api.RoomsApi
import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.domain.model.params.CreateRoomParams
import com.xsofty.rooms.domain.repository.RoomsRepository
import com.xsofty.shared.Result
import com.xsofty.shared.di.CoroutinesModule.IoDispatcher
import com.xsofty.shared.network.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RoomsRepositoryImpl @Inject constructor(
    private val api: RoomsApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RoomsRepository {

    override suspend fun getRoomsByCategory(categoryId: String): Result<List<RoomEntity>> {
        return safeApiCall(dispatcher) {
            api.getRoomsByCategory(categoryId).map { it.toRoomEntity() }
        }
    }

    override suspend fun getMyRooms(): Result<List<RoomEntity>> {
        return safeApiCall(dispatcher) {
            api.getMyRooms().map { it.toRoomEntity() }
        }
    }

    override suspend fun createRoom(params: CreateRoomParams): Result<Unit> {
        return safeApiCall(dispatcher) {
            api.createRoom(params)
        }
    }
}