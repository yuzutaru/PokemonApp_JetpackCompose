package com.yustar.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yustar.auth.domain.LoginUserUseCase
import com.yustar.auth.presentation.event.LoginUiEvent
import com.yustar.auth.presentation.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Yustar Pramudana on 06/03/26.
 */

class LoginViewModel(private val loginUseCase: LoginUserUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(onSuccess: () -> Unit) {
        if (_uiState.value.email.isEmpty() || _uiState.value.password.isEmpty()) {
            _uiState.update { it.copy(error = "Email and password are required") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }
            val success = loginUseCase(_uiState.value.email, _uiState.value.password)

            if (success) {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Invalid credentials") }
            }
        }
    }

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChanged -> _uiState.update { it.copy(email = event.email) }
            is LoginUiEvent.OnPasswordChanged -> _uiState.update { it.copy(password = event.password) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = "") }
    }
}