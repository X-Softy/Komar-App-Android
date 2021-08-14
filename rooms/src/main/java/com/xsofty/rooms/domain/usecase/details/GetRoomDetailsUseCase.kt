package com.xsofty.rooms.domain.usecase.details

import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class GetRoomDetailsUseCase @Inject constructor(
    private val repository: RoomDetailsRepository
) {
    suspend operator fun invoke(roomId: String): Result<RoomDetailsEntity> {
        return repository.getRoomDetails(roomId)
    }
}