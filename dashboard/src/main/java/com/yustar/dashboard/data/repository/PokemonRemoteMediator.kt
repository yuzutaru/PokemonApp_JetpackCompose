package com.yustar.dashboard.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yustar.dashboard.data.local.PokemonDatabase
import com.yustar.dashboard.data.local.PokemonEntity
import com.yustar.dashboard.data.remote.network.PokemonApiService

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val apiService: PokemonApiService,
    private val database: PokemonDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            // Determine page index
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) 0 else lastItem.page + 1
                }
            }

            // Fetch from PokeAPI (offset = page * limit)
            val pageSize = state.config.pageSize
            val response = apiService.getPokemonList(
                limit = pageSize,
                offset = loadKey * pageSize
            )

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.pokemonDao().clearAll()
                }
                val entities = response.results.map {
                    PokemonEntity(name = it.name, url = it.url, page = loadKey)
                }
                database.pokemonDao().insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}