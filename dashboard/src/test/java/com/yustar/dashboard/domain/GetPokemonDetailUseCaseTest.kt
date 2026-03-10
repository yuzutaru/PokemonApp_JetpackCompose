package com.yustar.dashboard.domain

import com.yustar.dashboard.data.remote.model.PokemonDetailResponse
import com.yustar.dashboard.data.remote.model.SpritesDto
import com.yustar.dashboard.data.remote.model.TypeDto
import com.yustar.dashboard.data.remote.model.TypeSlotDto
import com.yustar.dashboard.data.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Yustar Pramudana on 10/03/26.
 */

class GetPokemonDetailUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase

    @Before
    fun setUp() {
        getPokemonDetailUseCase = GetPokemonDetailUseCase(repository)
    }

    @Test
    fun `when invoke is called, return pokemon detail from repository`() = runTest {
        // Given
        val pokemonName = "pikachu"
        val expectedResponse = PokemonDetailResponse(
            id = 25,
            name = pokemonName,
            height = 4,
            weight = 60,
            sprites = SpritesDto(frontDefault = "url"),
            types = listOf(TypeSlotDto(slot = 1, type = TypeDto(name = "electric")))
        )
        coEvery { repository.getPokemonDetail(pokemonName) } returns expectedResponse

        // When
        val result = getPokemonDetailUseCase(pokemonName)

        // Then
        assertEquals(expectedResponse, result)
        coVerify { repository.getPokemonDetail(pokemonName) }
    }

    @Test(expected = Exception::class)
    fun `when repository throws exception, propagate it`() = runTest {
        // Given
        val pokemonName = "unknown"
        coEvery { repository.getPokemonDetail(pokemonName) } throws Exception("Not Found")

        // When
        getPokemonDetailUseCase(pokemonName)

        // Then
        // Exception is expected
    }
}
