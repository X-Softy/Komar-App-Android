package com.xsofty.rooms.presentation.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.model.params.AddCommentParams
import com.xsofty.rooms.domain.usecase.details.AddCommentUseCase
import com.xsofty.rooms.domain.usecase.details.GetRoomDetailsUseCase
import com.xsofty.rooms.domain.usecase.details.status.DeleteRoomUseCase
import com.xsofty.rooms.domain.usecase.details.status.JoinRoomUseCase
import com.xsofty.rooms.domain.usecase.details.status.RoomDetailsStatusUseCase
import com.xsofty.rooms.domain.usecase.details.status.UnjoinRoomUseCase
import com.xsofty.shared.Result
import com.xsofty.shared.base.BaseViewModel
import com.xsofty.shared.ext.handleLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val getRoomDetailsUseCase: GetRoomDetailsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val unjoinRoomUseCase: UnjoinRoomUseCase
) : BaseViewModel() {

    private val roomDetailsRequestFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val roomDetails: MutableState<Result<RoomDetailsEntity>> = mutableStateOf(Result.Loading)

    private val addCommentRequestFlow = MutableSharedFlow<Pair<String, AddCommentParams>>(extraBufferCapacity = 1)
    val addCommentStatus: MutableState<Result<Unit>> = mutableStateOf(Result.Loading)

    private val deleteRoomStatusRequestFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val deleteRoomStatus: MutableState<Result<Unit>> = mutableStateOf(Result.Loading)

    private val joinRoomStatusRequestFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val joinRoomStatus: MutableState<Result<Unit>> = mutableStateOf(Result.Loading)

    private val unjoinRoomStatusRequestFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val unjoinRoomStatus: MutableState<Result<Unit>> = mutableStateOf(Result.Loading)

    init {
        viewModelScope.launch {
            roomDetailsRequestFlow
                .handleLoading(roomDetails)
                .map { roomId -> getRoomDetailsUseCase(roomId) }
                .collect { roomDetails.value = it }
        }
        viewModelScope.launch {
            addCommentRequestFlow
                .handleLoading(addCommentStatus)
                .map {
                    val (roomId, addCommentParams) = it
                    addCommentUseCase(roomId, addCommentParams)
                }
                .collect { addCommentStatus.value = it }
        }
        initStatusService(deleteRoomStatusRequestFlow, deleteRoomStatus, deleteRoomUseCase)
        initStatusService(joinRoomStatusRequestFlow, joinRoomStatus, joinRoomUseCase)
        initStatusService(unjoinRoomStatusRequestFlow, unjoinRoomStatus, unjoinRoomUseCase)
    }

    private fun initStatusService(
        statusRequestFlow: MutableSharedFlow<String>,
        status: MutableState<Result<Unit>>,
        detailsStatusUseCase: RoomDetailsStatusUseCase
    ) {
        viewModelScope.launch {
            statusRequestFlow
                .handleLoading(status)
                .map { roomId -> detailsStatusUseCase(roomId) }
                .collect { status.value = it }
        }
    }

    fun requestRoomDetails(roomId: String) {
        roomDetailsRequestFlow.tryEmit(roomId)
    }

    fun addComment(roomId: String, text: String) {
        addCommentRequestFlow.tryEmit(
            roomId to AddCommentParams(text)
        )
    }

    fun deleteRoom(roomId: String) {
        deleteRoomStatusRequestFlow.tryEmit(roomId)
    }

    fun joinRoom(roomId: String) {
        joinRoomStatusRequestFlow.tryEmit(roomId)
    }

    fun unjoinRoom(roomId: String) {
        unjoinRoomStatusRequestFlow.tryEmit(roomId)
    }
}