package com.yustar.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yustar.dashboard.domain.GetPokemonUseCase
import com.yustar.dashboard.presentation.event.DashboardUiEvent
import com.yustar.dashboard.presentation.state.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

class DashboardViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.OnMenuSelected -> _uiState.update { it.copy(selectedTab = event.index) }
        }
    }
}
