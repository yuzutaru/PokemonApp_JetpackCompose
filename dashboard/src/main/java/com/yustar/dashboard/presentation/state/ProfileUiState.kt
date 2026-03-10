package com.yustar.dashboard.presentation.state

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoggedOut: Boolean = false,
    val error: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val phoneNumber: String = ""
)
