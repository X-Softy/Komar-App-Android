package com.xsofty.rooms.presentation.myrooms

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.domain.usecase.rooms.GetMyRoomsUseCase
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
class MyRoomsViewModel @Inject constructor(
    private val getMyRoomsUseCase: GetMyRoomsUseCase
) : BaseViewModel() {

    private val myRoomsRequestFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val myRooms: MutableState<Result<List<RoomEntity>>> = mutableStateOf(Result.Loading)

    init {
        viewModelScope.launch {
            myRoomsRequestFlow
                .handleLoading(myRooms)
                .map { getMyRoomsUseCase() }
                .collect { myRooms.value = it }
        }
    }

    fun requestMyRooms() {
        myRoomsRequestFlow.tryEmit(Unit)
    }
}