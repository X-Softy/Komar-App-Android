package com.xsofty.shared.ext

import androidx.compose.runtime.MutableState
import com.xsofty.shared.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun <T, R> Flow<T>.handleLoading(state: MutableState<Result<R>>) = onEach {
    state.value = Result.Loading
}