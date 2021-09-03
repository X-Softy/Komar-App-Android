package com.xsofty.rooms.domain.usecase.details.status

import com.xsofty.rooms.domain.model.entity.RoomStatus
import com.xsofty.shared.Result

interface RoomDetailsStatusUseCase {
    suspend operator fun invoke(roomId: String): Result<RoomStatus>
}