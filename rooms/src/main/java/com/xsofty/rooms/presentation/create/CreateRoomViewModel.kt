package com.xsofty.rooms.presentation.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.rooms.domain.model.params.CreateRoomParams
import com.xsofty.categories.domain.usecase.GetCategoriesUseCase
import com.xsofty.rooms.domain.usecase.rooms.CreateRoomUseCase
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
class CreateRoomViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createRoomUseCase: CreateRoomUseCase
) : BaseViewModel() {

    private val createRoomRequestFlow = MutableSharedFlow<CreateRoomParams>(extraBufferCapacity = 1)
    val createRoomStatus: MutableState<Result<Unit>> = mutableStateOf(Result.Loading)

    private val categoriesRequestFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val categories: MutableState<Result<List<CategoryEntity>>> = mutableStateOf(Result.Loading)

    init {
        viewModelScope.launch {
            categoriesRequestFlow
                .handleLoading(categories)
                .map { getCategoriesUseCase() }
                .collect { categories.value = it }
        }
        viewModelScope.launch {
            createRoomRequestFlow
                .handleLoading(createRoomStatus)
                .map { params -> createRoomUseCase(params) }
                .collect { createRoomStatus.value = it }
        }
    }

    fun requestCategories() {
        categoriesRequestFlow.tryEmit(Unit)
    }

    fun createRoom(
        categoryId: String,
        title: String,
        description: String
    ) {
        createRoomRequestFlow.tryEmit(
            CreateRoomParams(
                categoryId, title, description
            )
        )
    }
}