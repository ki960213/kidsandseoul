package com.ki960213.kidsandseoul.presentation.common.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected fun <T> Flow<T>.viewModelStateIn(
        started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
        initialValue: T,
    ): StateFlow<T> = catch { Log.d("THOMAS", it.toString()) }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = initialValue,
    )

    protected fun <T> Flow<T>.viewModelMutableStateIn(initialValue: T): MutableStateFlow<T> {
        val flow = MutableStateFlow(initialValue)

        viewModelScope.launch {
            this@viewModelMutableStateIn.collect(flow)
        }

        return flow
    }
}
