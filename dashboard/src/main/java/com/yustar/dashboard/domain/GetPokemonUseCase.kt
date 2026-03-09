package com.yustar.dashboard.domain

import androidx.paging.PagingData
import com.yustar.dashboard.data.local.PokemonEntity
import com.yustar.dashboard.data.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

class GetPokemonUseCase(
    private val repository: PokemonRepository
) {
    operator fun invoke(): Flow<PagingData<PokemonEntity>> {
        return repository.getPokemon()
    }
}
