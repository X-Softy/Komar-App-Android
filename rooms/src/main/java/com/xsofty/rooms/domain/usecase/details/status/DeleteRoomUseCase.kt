package com.xsofty.rooms.domain.usecase.details.status

import com.xsofty.rooms.domain.model.entity.RoomStatus
import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.shared.Result
import timber.log.Timber
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor(
    private val repository: RoomDetailsRepository
) : RoomDetailsStatusUseCase {
    override suspend operator fun invoke(roomId: String): Result<RoomStatus> {
        val response = repository.deleteRoom(roomId)
        return if (response is Result.Success) {
            Result.Success(RoomStatus.INACTIVE)
        } else {
            Result.Error((response as Result.Error).errorResponse)
        }
    }
}