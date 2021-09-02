package com.xsofty.rooms.domain.repository

import com.xsofty.rooms.domain.model.entity.CommentEntity
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.model.params.AddCommentParams
import com.xsofty.shared.Result

interface RoomDetailsRepository {
    suspend fun getRoomDetails(roomId: String): Result<RoomDetailsEntity>
    suspend fun addComment(roomId: String, params: AddCommentParams): Result<Unit>
    suspend fun getComments(roomId: String): Result<List<CommentEntity>>
    suspend fun deleteRoom(roomId: String): Result<Unit>
    suspend fun joinRoom(roomId: String): Result<Unit>
    suspend fun unjoinRoom(roomId: String): Result<Unit>
}