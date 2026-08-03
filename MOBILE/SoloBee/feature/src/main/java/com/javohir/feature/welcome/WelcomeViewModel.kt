package com.javohir.feature.welcome
import androidx.lifecycle.viewModelScope
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.welcome
 * Description: ViewModel
 */
@HiltViewModel
class WelcomeViewModel @Inject constructor(
): BaseViewModel<WelcomeState, WelcomeEvent>(initialState = WelcomeState()) {

    fun onAction(intent: WelcomeIntent){
        when(intent){
            is WelcomeIntent.LoginClicked -> {
                viewModelScope.launch {
                    emitEvent(event = WelcomeEvent.NavigateToLogin)
                }
            }
            is WelcomeIntent.SignUpClicked -> {
                viewModelScope.launch {
                    emitEvent(event = WelcomeEvent.NavigateToRegister)
                }
            }
        }
    }
}