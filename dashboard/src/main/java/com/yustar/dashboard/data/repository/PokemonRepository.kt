package com.yustar.dashboard.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yustar.dashboard.data.local.PokemonDatabase
import com.yustar.dashboard.data.local.PokemonEntity
import com.yustar.dashboard.data.remote.model.PokemonDetailResponse
import com.yustar.dashboard.data.remote.network.PokemonApiService
import kotlinx.coroutines.flow.Flow

class PokemonRepository(
    private val apiService: PokemonApiService,
    private val database: PokemonDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPokemon(): Flow<PagingData<PokemonEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            remoteMediator = PokemonRemoteMediator(apiService, database),
            pagingSourceFactory = { database.pokemonDao().getPagingSource() }
        ).flow
    }

    suspend fun getPokemonDetail(name: String): PokemonDetailResponse {
        return apiService.getPokemonDetail(name)
    }
}
