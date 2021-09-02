package com.xsofty.rooms.domain.usecase.details

import com.xsofty.rooms.domain.model.entity.CommentEntity
import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: RoomDetailsRepository
){
    suspend operator fun invoke(roomId: String): Result<List<CommentEntity>> {
        return repository.getComments(roomId)
    }
}