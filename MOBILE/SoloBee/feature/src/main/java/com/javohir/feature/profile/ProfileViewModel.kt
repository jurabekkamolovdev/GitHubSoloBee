package com.javohir.feature.profile

import androidx.lifecycle.viewModelScope
import com.javohir.domain.common.Resource
import com.javohir.domain.model.DeleteAccountResult
import com.javohir.domain.useCase.DeleteStudentAccountUseCase
import com.javohir.domain.useCase.LoadStudentProfileUseCase
import com.javohir.utils.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.profile
 * Description: ViewModel
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loadStudentProfileUseCase: LoadStudentProfileUseCase,
    private val deleteStudentAccountUseCase: DeleteStudentAccountUseCase,
) : BaseViewModel<ProfileState, ProfileEvent>(initialState = ProfileState()) {

    init {
        loadProfile()
    }

    fun onAction(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.DeleteAccountClicked -> showDeleteAccountDialog()
            ProfileIntent.DeleteAccountConfirmed -> deleteAccount()
            ProfileIntent.DeleteAccountDismissed -> dismissDeleteAccountDialog()
            ProfileIntent.ProfileSettingsClicked -> showComingSoon()
            ProfileIntent.LanguageClicked -> showComingSoon()
            ProfileIntent.SoundClicked -> showComingSoon()
            ProfileIntent.ParentZoneClicked -> showComingSoon()
            ProfileIntent.ChangeAvatarClicked -> showComingSoon()
        }
    }

    private fun showComingSoon() {
        viewModelScope.launch {
            emitEvent(ProfileEvent.ShowComingSoon)
        }
    }

    private fun showDeleteAccountDialog() {
        updateState { state -> state.copy(showDeleteAccountDialog = true) }
    }

    private fun dismissDeleteAccountDialog() {
        updateState { state -> state.copy(showDeleteAccountDialog = false) }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { state -> state.copy(isLoadingProfile = true) }
            when (val result = loadStudentProfileUseCase(forceRefresh = true)) {
                is Resource.Success -> {
                    updateState { state ->
                        state.copy(
                            isLoadingProfile = false,
                            fullName = result.data.fullName,
                            userInitial = result.data.userInitial,
                        )
                    }
                }
                is Resource.Error -> {
                    updateState { state -> state.copy(isLoadingProfile = false) }
                    emitEvent(ProfileEvent.ShowError(message = result.message))
                }
                is Resource.Unauthorized -> {
                    updateState { state -> state.copy(isLoadingProfile = false) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            updateState { state -> state.copy(isDeletingAccount = true) }
            when (val result = deleteStudentAccountUseCase()) {
                DeleteAccountResult.Success -> {
                    updateState { state ->
                        state.copy(
                            isDeletingAccount = false,
                            showDeleteAccountDialog = false,
                        )
                    }
                }
                is DeleteAccountResult.Error -> {
                    updateState { state ->
                        state.copy(
                            isDeletingAccount = false,
                            showDeleteAccountDialog = false,
                        )
                    }
                    emitEvent(ProfileEvent.ShowError(message = result.message))
                }
            }
        }
    }
}
