package com.javohir.feature.register
import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.model.AvatarGender
import com.javohir.domain.useCase.GetAvatarsUseCase
import com.javohir.domain.useCase.RegisterUseCase
import com.javohir.feature.R
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register
 * Description: ViewModel
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val getAvatarsUseCase: GetAvatarsUseCase
): BaseViewModel<RegisterState, RegisterEvent>(initialState = RegisterState()) {

    fun onAction(intent: RegisterIntent){
        when(intent){
            is RegisterIntent.FirstNameChanged -> {
                updateState { state -> state.copy(firstName = intent.value) }
            }
            is RegisterIntent.LastNameChanged -> {
                updateState { state -> state.copy(lastName = intent.value) }
            }
            is RegisterIntent.AgeChanged -> {
                // Faqat raqam: klaviatura Number bo'lsa ham qo'yib yuborilgan belgilar bo'lishi mumkin
                val digits = intent.value.filter { char -> char.isDigit() }.take(n = 2)
                updateState { state -> state.copy(age = digits) }
            }
            is RegisterIntent.UserNameChanged -> {
                updateState { state -> state.copy(userName = intent.value) }
            }
            is RegisterIntent.PasswordChanged -> {
                updateState { state -> state.copy(password = intent.value) }
            }
            is RegisterIntent.TogglePasswordVisibility -> {
                updateState { state -> state.copy(isPasswordVisible = !state.isPasswordVisible) }
            }
            is RegisterIntent.GenderChanged -> changeGender(gender = intent.gender)

            is RegisterIntent.AvatarSelected -> {
                updateState { state -> state.copy(selectedAvatarId = intent.avatarId) }
            }
            is RegisterIntent.ContinueClicked -> goToNextStep()

            is RegisterIntent.BackClicked -> goToPreviousStep()

            is RegisterIntent.SignUpClicked -> register()

            is RegisterIntent.SuccessShown -> {
                viewModelScope.launch {
                    emitEvent(event = RegisterEvent.NavigateToLogin)
                }
            }
            is RegisterIntent.RetryAvatarsClicked -> loadAvatars()
        }
    }

    private fun goToNextStep(){
        when(getCurrentState().step){
            RegisterStep.PERSONAL_INFO -> {
                if (validatePersonalInfo()) {
                    updateState { state -> state.copy(step = RegisterStep.ACCOUNT_INFO) }
                }
            }
            RegisterStep.ACCOUNT_INFO -> {
                if (validateAccountInfo()) {
                    updateState { state -> state.copy(step = RegisterStep.SELECT_AVATAR) }
                    loadAvatars()
                }
            }
            RegisterStep.SELECT_AVATAR, RegisterStep.SUCCESS -> Unit
        }
    }

    private fun goToPreviousStep(){
        val previous = when(getCurrentState().step){
            RegisterStep.PERSONAL_INFO -> null
            RegisterStep.ACCOUNT_INFO -> RegisterStep.PERSONAL_INFO
            RegisterStep.SELECT_AVATAR -> RegisterStep.ACCOUNT_INFO
            RegisterStep.SUCCESS -> null
        }

        if (previous == null) {
            viewModelScope.launch {
                emitEvent(event = RegisterEvent.NavigateBack)
            }
            return
        }
        updateState { state -> state.copy(step = previous) }
    }

    private fun validatePersonalInfo(): Boolean {
        val state = getCurrentState()

        if (state.firstName.isBlank()){
            showError(messageId = R.string.register_first_name_required)
            return false
        }
        if (state.lastName.isBlank()){
            showError(messageId = R.string.register_last_name_required)
            return false
        }

        val age = state.age.toIntOrNull()
        if (age == null){
            showError(messageId = R.string.register_age_required)
            return false
        }
        if (age !in MIN_AGE..MAX_AGE){
            showError(messageId = R.string.register_age_invalid)
            return false
        }
        return true
    }

    private fun validateAccountInfo(): Boolean {
        val state = getCurrentState()
        val userName = state.userName.trim()
        val password = state.password.trim()

        // Tekshiruv login ekranidagi bilan bir xil
        if (userName.isBlank() || password.isBlank()){
            showError(messageId = R.string.login_username_password_required)
            return false
        }
        if (password.length <= 6){
            showError(messageId = R.string.login_password_too_short)
            return false
        }
        return true
    }

    private fun changeGender(gender: AvatarGender){
        if (getCurrentState().gender == gender) return

        updateState { state ->
            val firstOfGender = state.avatars.firstOrNull { avatar -> avatar.gender == gender }
            state.copy(
                gender = gender,
                // Jins almashganda tanlov ko'rinmas qolib ketmasin
                selectedAvatarId = firstOfGender?.id ?: state.selectedAvatarId
            )
        }
    }

    private fun loadAvatars(){
        if (getCurrentState().isAvatarsLoading) return

        viewModelScope.launch {
            updateState { state -> state.copy(isAvatarsLoading = true) }

            when(val result = getAvatarsUseCase()){
                is Resource.Success -> {
                    updateState { state ->
                        val firstOfGender = result.data.firstOrNull { avatar -> avatar.gender == state.gender }
                        state.copy(
                            avatars = result.data,
                            isAvatarsLoading = false,
                            // Birinchi avatar avtomatik tanlanadi
                            selectedAvatarId = state.selectedAvatarId ?: firstOfGender?.id
                        )
                    }
                    if (result.data.isEmpty()){
                        showError(messageId = R.string.register_avatars_empty)
                    }
                }
                is Resource.Error -> {
                    updateState { state -> state.copy(isAvatarsLoading = false) }
                    emitEvent(event = RegisterEvent.ShowErrorText(message = result.message))
                }
                is Resource.Unauthorized -> {
                    updateState { state -> state.copy(isAvatarsLoading = false) }
                    showError(messageId = R.string.register_avatars_empty)
                }
                is Resource.Loading -> {
                    updateState { state -> state.copy(isAvatarsLoading = true) }
                }
            }
        }
    }

    private fun register(){
        val state = getCurrentState()
        val age = state.age.toIntOrNull()
        val avatarId = state.selectedAvatarId

        if (age == null || age !in MIN_AGE..MAX_AGE){
            showError(messageId = R.string.register_age_invalid)
            return
        }
        if (avatarId.isNullOrBlank()){
            showError(messageId = R.string.register_avatar_required)
            return
        }

        viewModelScope.launch {
            updateState { current -> current.copy(isLoading = true) }

            when(val result = registerUseCase(
                firstName = state.firstName,
                lastName = state.lastName,
                userName = state.userName,
                password = state.password,
                age = age,
                avatarId = avatarId
            )){
                is Resource.Success -> {
                    updateState { current ->
                        current.copy(isLoading = false, step = RegisterStep.SUCCESS)
                    }
                }
                is Resource.Error -> {
                    updateState { current -> current.copy(isLoading = false) }
                    emitEvent(event = RegisterEvent.ShowErrorText(message = result.message))
                }
                is Resource.Unauthorized -> {
                    updateState { current -> current.copy(isLoading = false) }
                    showError(messageId = R.string.login_username_password_required)
                }
                is Resource.Loading -> {
                    updateState { current -> current.copy(isLoading = true) }
                }
            }
        }
    }

    private fun showError(messageId: Int){
        viewModelScope.launch {
            emitEvent(event = RegisterEvent.ShowErrorRes(messageId = messageId))
        }
    }

    private companion object {
        const val MIN_AGE = 3
        const val MAX_AGE = 12
    }
}