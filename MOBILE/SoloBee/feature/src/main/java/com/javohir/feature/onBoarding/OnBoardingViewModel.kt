package com.javohir.feature.onBoarding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javohir.domain.useCase.MarkOnboardingCompletedUseCase
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.onBoarding
 * Description: ViewModel
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val markOnboardingCompletedUseCase: MarkOnboardingCompletedUseCase,
): BaseViewModel<OnBoardingState, OnBoardingEvent>(initialState = OnBoardingState()) {


    fun onAction(intent: OnBoardingIntent){
        when(intent){
            is OnBoardingIntent.PageChanged ->  pageChanged(intent = intent)
            is OnBoardingIntent.SkipClicked -> skipClicked()
            is OnBoardingIntent.NextClicked -> nextClicked()
        }
    }

    private fun skipClicked(){
        viewModelScope.launch {
            markOnboardingCompletedUseCase()
            emitEvent(OnBoardingEvent.NavigateToWelcome)
        }
    }
    private fun nextClicked(){
        val currentState = getCurrentState()
        val current = currentState.currentPage
        val lastIndex = currentState.pages.lastIndex
        if (current < lastIndex){
            updateState { state -> state.copy(currentPage = current + 1) }
        } else {
            viewModelScope.launch {
                markOnboardingCompletedUseCase()
                emitEvent(OnBoardingEvent.NavigateToWelcome)
            }
        }
    }
    private fun pageChanged(intent: OnBoardingIntent.PageChanged){
        updateState { state -> state.copy(currentPage = intent.page) }
    }
}