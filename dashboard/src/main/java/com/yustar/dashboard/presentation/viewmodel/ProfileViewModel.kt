package com.yustar.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yustar.core.session.SessionManager
import com.yustar.dashboard.domain.GetUserUseCase
import com.yustar.dashboard.domain.UpdateUserProfileUseCase
import com.yustar.dashboard.presentation.event.ProfileUiEvent
import com.yustar.dashboard.presentation.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

class ProfileViewModel(
    private val sessionManager: SessionManager,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val username = sessionManager.loggedUser.firstOrNull()
            if (username != null) {
                val user = getUserUseCase(username)
                if (user != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            address = user.address,
                            phoneNumber = user.phoneNumber
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "User not found") }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Session expired") }
            }
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnFirstNameChanged -> {
                _uiState.update { it.copy(firstName = event.firstName) }
            }

            is ProfileUiEvent.OnLastNameChanged -> {
                _uiState.update { it.copy(lastName = event.lastName) }
            }

            is ProfileUiEvent.OnAddressChanged -> {
                _uiState.update { it.copy(address = event.address) }
            }

            is ProfileUiEvent.OnPhoneNumberChanged -> {
                _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
            }

            is ProfileUiEvent.OnSaveClick -> {
                saveProfile()
            }

            is ProfileUiEvent.OnLogoutClick -> {
                logout()
            }

            is ProfileUiEvent.ClearError -> {
                _uiState.update { it.copy(error = "") }
            }

            is ProfileUiEvent.ResetSuccess -> {
                _uiState.update { it.copy(isSuccess = false) }
            }

            is ProfileUiEvent.ResetLogoutState -> {
                _uiState.update { it.copy(isLoggedOut = false) }
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.update { it.copy(isLoading = true, isSuccess = false) }
            val username = sessionManager.loggedUser.firstOrNull()
            if (username != null) {
                try {
                    updateUserProfileUseCase(
                        username = username,
                        firstName = currentState.firstName,
                        lastName = currentState.lastName,
                        address = currentState.address,
                        phoneNumber = currentState.phoneNumber
                    )
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Session expired") }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            sessionManager.logout()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}
