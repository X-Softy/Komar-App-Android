package com.xsofty.rooms.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.rooms.domain.model.RoomEntity
import com.xsofty.rooms.domain.usecase.GetRoomsUseCase
import com.xsofty.shared.Result
import com.xsofty.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RoomsViewModel @Inject constructor(
    private val getRoomsUseCase: GetRoomsUseCase
) : BaseViewModel() {

    val rooms: MutableState<Result<List<RoomEntity>>> = mutableStateOf(
        Result.Success(listOf())
    )

    init {
        viewModelScope.launch {
            rooms.value = getRoomsUseCase("f0iBHeS1K703EtA1LL7a")
        }
    }
}