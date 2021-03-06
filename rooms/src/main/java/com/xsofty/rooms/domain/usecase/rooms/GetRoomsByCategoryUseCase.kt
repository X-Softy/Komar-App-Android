package com.xsofty.rooms.domain.usecase.rooms

import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.domain.repository.RoomsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class GetRoomsByCategoryUseCase @Inject constructor(
    private val repository: RoomsRepository
) {
    suspend operator fun invoke(categoryId: String): Result<List<RoomEntity>> {
        return repository.getRoomsByCategory(categoryId)
    }
}