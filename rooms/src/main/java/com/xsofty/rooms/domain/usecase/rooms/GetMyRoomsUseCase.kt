package com.xsofty.rooms.domain.usecase.rooms

import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.domain.repository.RoomsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class GetMyRoomsUseCase @Inject constructor(
    private val repository: RoomsRepository
) {
    suspend operator fun invoke(): Result<List<RoomEntity>> {
        return repository.getMyRooms()
    }
}