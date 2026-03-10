package com.yustar.dashboard.presentation.state

import com.yustar.dashboard.data.remote.model.PokemonDetailResponse

/**
 * Created by Yustar Pramudana on 10/03/26.
 */

data class DetailUiState (
    val isLoading: Boolean = false,
    val pokemon: PokemonDetailResponse? = null,
    val error: String = "",
)