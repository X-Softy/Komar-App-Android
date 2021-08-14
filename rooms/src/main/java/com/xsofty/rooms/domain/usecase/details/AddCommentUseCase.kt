package com.xsofty.rooms.domain.usecase.details

import com.xsofty.rooms.domain.model.params.AddCommentParams
import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.shared.Result
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: RoomDetailsRepository
) {
    suspend operator fun invoke(roomId: String, params: AddCommentParams): Result<Unit> {
        return repository.addComment(roomId, params)
    }
}