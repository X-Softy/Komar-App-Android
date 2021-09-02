package com.xsofty.rooms.data.repository

import com.xsofty.categories.data.network.api.CategoriesApi
import com.xsofty.rooms.data.network.api.RoomDetailsApi
import com.xsofty.rooms.domain.model.entity.CommentEntity
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.model.params.AddCommentParams
import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.shared.Result
import com.xsofty.shared.di.CoroutinesModule.IoDispatcher
import com.xsofty.shared.network.safeApiCall
import com.xsofty.shared.storage.AppPreferences
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RoomDetailsRepositoryImpl @Inject constructor(
    private val roomDetailsApi: RoomDetailsApi,
    private val categoriesApi: CategoriesApi,
    private val appPreferences: AppPreferences,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RoomDetailsRepository {

    override suspend fun getRoomDetails(roomId: String): Result<RoomDetailsEntity> {
        return safeApiCall(dispatcher) {
            val roomDetailsDto = roomDetailsApi.getRoomDetails(roomId)
            val category = categoriesApi.getCategories().first {
                it.id == roomDetailsDto.categoryId
            }.toCategoryEntity()

            val userId = appPreferences.userId
            if (userId == null) {
                throw Exception("User id is null")
            } else {
                roomDetailsDto.toRoomDetailsEntity(category, userId)
            }
        }
    }

    override suspend fun getComments(roomId: String): Result<List<CommentEntity>> {
        return safeApiCall(dispatcher) {
            roomDetailsApi.getRoomDetails(roomId).comments.map {
                it.toCommentEntity()
            }
        }
    }

    override suspend fun addComment(roomId: String, params: AddCommentParams): Result<Unit> {
        return safeApiCall(dispatcher) {
            roomDetailsApi.addComment(roomId, params)
        }
    }

    override suspend fun deleteRoom(roomId: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            roomDetailsApi.deleteRoom(roomId)
        }
    }

    override suspend fun joinRoom(roomId: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            roomDetailsApi.joinRoom(roomId)
        }
    }

    override suspend fun unjoinRoom(roomId: String): Result<Unit> {
        return safeApiCall(dispatcher) {
            roomDetailsApi.unjoinRoom(roomId)
        }
    }
}