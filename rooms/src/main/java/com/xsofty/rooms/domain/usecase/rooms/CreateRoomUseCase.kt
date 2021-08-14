package com.xsofty.rooms.domain.usecase.rooms

import com.xsofty.rooms.domain.model.params.CreateRoomParams
import com.xsofty.rooms.domain.repository.RoomsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class CreateRoomUseCase @Inject constructor(
    private val repository: RoomsRepository
) {
    suspend operator fun invoke(params: CreateRoomParams): Result<Unit> {
        return repository.createRoom(params)
    }
}