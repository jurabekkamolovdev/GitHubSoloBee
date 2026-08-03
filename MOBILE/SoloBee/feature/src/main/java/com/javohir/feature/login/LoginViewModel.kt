package com.javohir.feature.login
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.useCase.LoginUseCase
import com.javohir.feature.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.javohir.utils.mvi.BaseViewModel

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.login
 * Description: ViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): BaseViewModel<LoginState, LoginEvent>(initialState = LoginState()) {

    fun onAction(intent: LoginIntent){
        when(intent){
            is LoginIntent.UserNameChanged -> {
                updateState { state -> state.copy(usernameText = intent.value) }
            }
            is LoginIntent.PasswordChanged -> {
                updateState { state -> state.copy(passwordText = intent.value) }
            }
            is LoginIntent.LoginClicked -> login()
        }
    }
    private fun login(){
        val username = getCurrentState().usernameText.trim()
        val password = getCurrentState().passwordText.trim()


        if (username.isBlank() || password.isBlank()){
            viewModelScope.launch {
                emitEvent(event = LoginEvent.ShowErrorRes(messageId = R.string.login_username_password_required))
            }
            return
        }

        if (password.length <= 6 ){
            viewModelScope.launch {
                emitEvent(event = LoginEvent.ShowErrorRes(messageId = R.string.login_password_too_short))
            }
            return
        }

        viewModelScope.launch {
            updateState { state -> state.copy(isLoading = true) }

            when(val result = loginUseCase(username = username, password = password)){
                is Resource.Success -> {
                    updateState { state -> state.copy(isLoading = false) }
                    emitEvent(event = LoginEvent.NavigateToHome)
                }
                is Resource.Error -> {
                    updateState { state -> state.copy(isLoading = false) }
                    emitEvent(event = LoginEvent.ShowErrorText(message = result.message))
                }
                is Resource.Unauthorized -> Unit
                is Resource.Loading -> {
                    updateState { state -> state.copy(isLoading = true) }
                }
            }
        }
    }
}
