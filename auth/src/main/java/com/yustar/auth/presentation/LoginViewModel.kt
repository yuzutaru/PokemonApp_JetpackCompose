package com.yustar.auth.presentation

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yustar.auth.domain.LoginUserUseCase
import com.yustar.auth.presentation.event.LoginUiEvent
import com.yustar.auth.presentation.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by Yustar Pramudana on 06/03/26.
 */

class LoginViewModel(private val loginUseCase: LoginUserUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val success = loginUseCase(_uiState.value.email, _uiState.value.password)

            if (success) {
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(error = "Invalid credentials")
            }
        }
    }

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChanged -> _uiState.value = _uiState.value.copy(email = event.email)
            is LoginUiEvent.OnPasswordChanged -> _uiState.value = _uiState.value.copy(password = event.password)
        }
    }
}