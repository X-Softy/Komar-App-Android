package com.xsofty.rooms.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.rooms.domain.model.RoomEntity
import com.xsofty.rooms.domain.usecase.GetRoomsUseCase
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
internal class RoomsViewModel @Inject constructor(
    private val getRoomsUseCase: GetRoomsUseCase
) : BaseViewModel() {

    private val roomsRequestFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val rooms: MutableState<Result<List<RoomEntity>>> = mutableStateOf(Result.Loading)

    init {
        viewModelScope.launch {
            roomsRequestFlow
                .handleLoading(rooms)
                .map { categoryId -> getRoomsUseCase(categoryId) }
                .collect { rooms.value = it }
        }
    }

    fun requestRooms(categoryId: String) {
        roomsRequestFlow.tryEmit(categoryId)
    }
}