package com.xsofty.categories.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.xsofty.categories.domain.model.CategoryEntity
import com.xsofty.categories.domain.usecase.GetCategoriesUseCase
import com.xsofty.shared.Result
import com.xsofty.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel() {

    val categories: MutableState<Result<List<CategoryEntity>>> = mutableStateOf(
        Result.Success(listOf())
    )

    init {
        viewModelScope.launch {
            categories.value = getCategoriesUseCase()
        }
    }
}