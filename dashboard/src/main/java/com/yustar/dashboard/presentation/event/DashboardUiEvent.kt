package com.yustar.dashboard.presentation.event

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

sealed class DashboardUiEvent {
    data class OnMenuSelected(val index: Int): DashboardUiEvent()
}