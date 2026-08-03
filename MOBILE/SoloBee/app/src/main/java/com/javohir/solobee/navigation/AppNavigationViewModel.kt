package com.javohir.solobee.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javohir.domain.model.SplashDestination
import com.javohir.domain.useCase.ObserveSessionExpiredUseCase
import com.javohir.domain.useCase.ResolveSplashDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    observeSessionExpiredUseCase: ObserveSessionExpiredUseCase,
    private val resolveSplashDestinationUseCase: ResolveSplashDestinationUseCase,
) : ViewModel() {

    private val _authDestination = MutableSharedFlow<SplashDestination>(extraBufferCapacity = 1)
    val authDestination: SharedFlow<SplashDestination> = _authDestination.asSharedFlow()

    init {
        viewModelScope.launch {
            observeSessionExpiredUseCase().collect {
                val destination: SplashDestination = resolveSplashDestinationUseCase()
                if (destination != SplashDestination.Home) {
                    _authDestination.emit(destination)
                }
            }
        }
    }
}
