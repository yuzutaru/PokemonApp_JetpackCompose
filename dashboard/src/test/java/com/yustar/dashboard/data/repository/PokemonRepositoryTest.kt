package com.yustar.dashboard.data.repository

import com.yustar.dashboard.data.local.PokemonDatabase
import com.yustar.dashboard.data.remote.model.PokemonDetailResponse
import com.yustar.dashboard.data.remote.network.PokemonApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PokemonRepositoryTest {

    private lateinit var repository: PokemonRepository
    private val apiService: PokemonApiService = mockk()
    private val database: PokemonDatabase = mockk()

    @Before
    fun setUp() {
        repository = PokemonRepository(apiService, database)
    }

    @Test
    fun `getPokemonDetail should call apiService and return detail response`() = runTest {
        // Given
        val pokemonName = "bulbasaur"
        val expectedResponse = mockk<PokemonDetailResponse>()
        coEvery { apiService.getPokemonDetail(pokemonName) } returns expectedResponse

        // When
        val result = repository.getPokemonDetail(pokemonName)

        // Then
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { apiService.getPokemonDetail(pokemonName) }
    }
}
