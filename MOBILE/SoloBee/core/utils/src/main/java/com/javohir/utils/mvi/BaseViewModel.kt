package com.javohir.utils.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<UiState : Any, UiEvent>(
    initialState: UiState,
) : ViewModel() {

    private val mutableState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val state: StateFlow<UiState> = mutableState.asStateFlow()

    private val mutableEvent: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val event: SharedFlow<UiEvent> = mutableEvent.asSharedFlow()

    protected fun getCurrentState(): UiState {
        return mutableState.value
    }

    protected fun updateState(transform: (UiState) -> UiState) {
        mutableState.update(transform)
    }

    protected suspend fun emitEvent(event: UiEvent) {
        mutableEvent.emit(event)
    }
}
