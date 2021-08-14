package com.xsofty.rooms.data.repository

import com.xsofty.rooms.data.network.api.RoomDetailsApi
import com.xsofty.rooms.data.network.api.RoomsApi
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.model.params.AddCommentParams
import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.shared.Result
import com.xsofty.shared.di.CoroutinesModule.IoDispatcher
import com.xsofty.shared.network.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RoomDetailsRepositoryImpl @Inject constructor(
    private val api: RoomDetailsApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RoomDetailsRepository {

    override suspend fun getRoomDetails(roomId: String): Result<RoomDetailsEntity> {
        return safeApiCall(dispatcher) {
            api.getRoomDetails(roomId).toRoomDetailsEntity()
        }
    }

    override suspend fun addComment(roomId: String, params: AddCommentParams): Result<Unit> {
        return safeApiCall(dispatcher) {
            api.addComment(roomId, params)
        }
    }

    override suspend fun deleteRoom(roomId: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            api.deleteRoom(roomId)
        }
    }

    override suspend fun joinRoom(roomId: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            api.joinRoom(roomId)
        }
    }

    override suspend fun unjoinRoom(roomId: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            api.unjoinRoom(roomId)
        }
    }
}