package com.xsofty.rooms.data.repository

import com.xsofty.rooms.data.network.RoomsApi
import com.xsofty.rooms.domain.model.RoomEntity
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

    override suspend fun getRooms(categoryId: String): Result<List<RoomEntity>> {
        return safeApiCall(dispatcher) {
            api.getRooms(categoryId).map { it.toRoomEntity() }
        }
    }
}