package com.yustar.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yustar.dashboard.domain.GetPokemonUseCase
import com.yustar.dashboard.presentation.state.MenuUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Yustar Pramudana on 09/03/26.
 */

class MenuViewModel(getPokemonUseCase: GetPokemonUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState

    val pokemonPagingData = getPokemonUseCase().cachedIn(viewModelScope)

}