package com.yustar.auth.presentation

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import com.yustar.auth.presentation.event.LoginUiEvent
import com.yustar.auth.presentation.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Yustar Pramudana on 06/03/26.
 */

class LoginViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChanged -> _uiState.value = _uiState.value.copy(email = event.email)
            is LoginUiEvent.OnPasswordChanged -> _uiState.value = _uiState.value.copy(password = event.password)
        }
    }
}