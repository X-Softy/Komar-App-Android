package com.xsofty.categories.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.categories.domain.model.CategoryEntity
import com.xsofty.categories.domain.usecase.GetCategoriesUseCase
import com.xsofty.shared.Result
import com.xsofty.shared.base.BaseViewModel
import com.xsofty.shared.ext.handleLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel() {

    private val categoriesRequestFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val categories: MutableState<Result<List<CategoryEntity>>> = mutableStateOf(Result.Loading)

    init {
        viewModelScope.launch {
            categoriesRequestFlow
                .handleLoading(categories)
                .map { getCategoriesUseCase() }
                .collect { categories.value = it }
        }
    }

    fun requestCategories() {
        categoriesRequestFlow.tryEmit(Unit)
    }
}

