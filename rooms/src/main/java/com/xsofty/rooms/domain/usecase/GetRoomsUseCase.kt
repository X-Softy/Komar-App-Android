package com.xsofty.rooms.domain.usecase

import com.xsofty.rooms.domain.model.RoomEntity
import com.xsofty.rooms.domain.repository.RoomsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class GetRoomsUseCase @Inject constructor(
    private val repository: RoomsRepository
) {
    suspend operator fun invoke(categoryId: String): Result<List<RoomEntity>> {
        return repository.getRooms(categoryId)
    }
}