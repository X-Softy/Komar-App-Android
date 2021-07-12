package com.xsofty.filter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xsofty.filter.domain.model.HelloWorldEntity
import com.xsofty.filter.domain.usecase.HelloWorldUseCase
import com.xsofty.shared.Result
import com.xsofty.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FilterViewModel @Inject constructor(
    private val helloWorldUseCase: HelloWorldUseCase
) : BaseViewModel() {

    private val helloWorldRequestFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val _helloWorldLiveData = MutableLiveData<Result<HelloWorldEntity>>()
    val helloWorldLiveData get() = _helloWorldLiveData

    init {
        viewModelScope.launch {
            helloWorldRequestFlow
                .onStart {
                    emit(Unit)
                }
                .onEach {
                    _helloWorldLiveData.value = Result.Loading
                }
                .mapLatest {
                    helloWorldUseCase.invoke()
                }
                .collect {
                    _helloWorldLiveData.value = it
                }
        }
    }
}