package com.yustar.dashboard.domain

import com.yustar.dashboard.data.repository.PokemonRepository

/**
 * Created by Yustar Pramudana on 10/03/26.
 */

class GetPokemonDetailUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String) = repository.getPokemonDetail(name)
}
