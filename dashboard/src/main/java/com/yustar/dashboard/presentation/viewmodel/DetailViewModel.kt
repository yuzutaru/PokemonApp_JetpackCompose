package com.yustar.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yustar.dashboard.domain.GetPokemonDetailUseCase
import com.yustar.dashboard.presentation.state.DetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Yustar Pramudana on 10/03/26.
 */

class DetailViewModel(private val getPokemonDetailUseCase: GetPokemonDetailUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun getPokemonDetail(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val pokemon = getPokemonDetailUseCase(name)
                _uiState.update { it.copy(isLoading = false, pokemon = pokemon) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown Error") }
            }
        }
    }
}