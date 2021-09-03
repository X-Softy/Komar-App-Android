package com.xsofty.rooms.presentation.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.rooms.domain.model.entity.CommentEntity
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.model.entity.RoomStatus
import com.xsofty.rooms.domain.model.params.AddCommentParams
import com.xsofty.rooms.domain.usecase.details.AddCommentUseCase
import com.xsofty.rooms.domain.usecase.details.GetCommentsUseCase
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val getRoomDetailsUseCase: GetRoomDetailsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    deleteRoomUseCase: DeleteRoomUseCase,
    joinRoomUseCase: JoinRoomUseCase,
    unjoinRoomUseCase: UnjoinRoomUseCase
) : BaseViewModel() {

    private val roomDetailsRequestFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val roomDetails: MutableState<Result<RoomDetailsEntity>> = mutableStateOf(Result.Loading)

    private val commentsRequestFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val comments: MutableState<Result<List<CommentEntity>>> = mutableStateOf(Result.Loading)
    private var timesCommentsFetched = 0

    private val addCommentRequestFlow =
        MutableSharedFlow<Pair<String, AddCommentParams>>(extraBufferCapacity = 1)
    val addCommentStatus: MutableState<Result<Int>> = mutableStateOf(Result.Loading)

    private val roomStatusRequestFlow = MutableSharedFlow<Pair<String, RoomStatus>>(extraBufferCapacity = 1)
    val roomStatus: MutableState<Result<RoomStatus>> = mutableStateOf(Result.Loading)

    init {
        viewModelScope.launch {
            roomDetailsRequestFlow
                .handleLoading(roomDetails)
                .map { roomId -> getRoomDetailsUseCase(roomId) }
                .collect {
                    roomDetails.value = it
                    if (it is Result.Success) {
                        roomStatus.value = Result.Success(it.data.roomStatus)
                    }
                }
        }
        viewModelScope.launch {
            addCommentRequestFlow
                .map {
                    val (roomId, addCommentParams) = it
                    addCommentUseCase(roomId, addCommentParams)
                }
                .collect {
                    addCommentStatus.value = when (it) {
                        is Result.Success -> {
                            timesCommentsFetched++
                            Result.Success(timesCommentsFetched)
                        }
                        else -> Result.Error()
                    }
                }
        }
        viewModelScope.launch {
            commentsRequestFlow
                .map { roomId -> getCommentsUseCase(roomId) }
                .collect {
                    comments.value = it
                }
        }
        viewModelScope.launch {
            roomStatusRequestFlow
                .map { (roomId, currentRoomStatus) ->
                    when (currentRoomStatus) {
                        RoomStatus.CREATOR -> deleteRoomUseCase(roomId)
                        RoomStatus.JOINED -> unjoinRoomUseCase(roomId)
                        RoomStatus.NOT_JOINED -> joinRoomUseCase(roomId)
                        else -> {}
                    }
                }
                .collect {
                    roomStatus.value = it as Result<RoomStatus>
                }
        }
    }

    fun changeRoomStatus(roomId: String, currentRoomStatus: RoomStatus) {
        roomStatusRequestFlow.tryEmit(
            roomId to currentRoomStatus
        )
    }

    fun requestRoomDetails(roomId: String) {
        roomDetailsRequestFlow.tryEmit(roomId)
    }

    fun requestComments(roomId: String) {
        commentsRequestFlow.tryEmit(roomId)
    }

    fun addComment(roomId: String, text: String) {
        addCommentRequestFlow.tryEmit(
            roomId to AddCommentParams(text)
        )
    }
}