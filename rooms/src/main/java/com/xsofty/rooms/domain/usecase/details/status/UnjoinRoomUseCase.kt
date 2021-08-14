package com.xsofty.rooms.domain.usecase.details.status

import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class UnjoinRoomUseCase @Inject constructor(
    private val repository: RoomDetailsRepository
) : RoomDetailsStatusUseCase {
    override suspend operator fun invoke(roomId: String): Result<Unit> {
        return repository.unjoinRoom(roomId)
    }
}