package com.yustar.dashboard.presentation.state

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

data class DashboardUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val selectedTab: Int = 0
)