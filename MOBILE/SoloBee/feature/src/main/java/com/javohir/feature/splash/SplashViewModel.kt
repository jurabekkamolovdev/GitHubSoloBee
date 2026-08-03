package com.javohir.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javohir.domain.model.SplashDestination
import com.javohir.domain.useCase.ResolveSplashDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.splash
 * Description: ViewModel
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val resolveSplashDestinationUseCase: ResolveSplashDestinationUseCase,
) : ViewModel() {

    private val _event = MutableSharedFlow<SplashEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(1500)
            when (resolveSplashDestinationUseCase()) {
                SplashDestination.Home -> _event.emit(SplashEvent.NavigateToHome)
                SplashDestination.Welcome -> _event.emit(SplashEvent.NavigateToWelcome)
                SplashDestination.OnBoarding -> _event.emit(SplashEvent.NavigateToOnBoarding)
            }
        }
    }
}