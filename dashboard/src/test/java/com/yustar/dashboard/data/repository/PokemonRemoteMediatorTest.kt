package com.yustar.dashboard.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yustar.dashboard.data.local.PokemonDao
import com.yustar.dashboard.data.local.PokemonDatabase
import com.yustar.dashboard.data.local.PokemonEntity
import com.yustar.dashboard.data.remote.model.PokemonDto
import com.yustar.dashboard.data.remote.model.PokemonResponse
import com.yustar.dashboard.data.remote.network.PokemonApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediatorTest {

    private val apiService: PokemonApiService = mockk()
    private val database: PokemonDatabase = mockk(relaxed = true)
    private val pokemonDao: PokemonDao = mockk()

    private lateinit var remoteMediator: PokemonRemoteMediator

    @Before
    fun setUp() {
        every { database.pokemonDao() } returns pokemonDao
        
        // Mock Room's withTransaction extension function
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        remoteMediator = PokemonRemoteMediator(apiService, database)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `refresh load returns Success when more data is present`() = runTest {
        // Arrange
        val pokemonList = listOf(
            PokemonDto(name = "bulbasaur", url = "url1"),
            PokemonDto(name = "ivysaur", url = "url2")
        )
        val response = PokemonResponse(
            count = 100,
            next = "next_url",
            previous = null,
            results = pokemonList
        )

        coEvery { apiService.getPokemonList(limit = any(), offset = any()) } returns response
        coEvery { pokemonDao.clearAll() } returns Unit
        coEvery { pokemonDao.insertAll(any()) } returns Unit

        val pagingState = PagingState<Int, PokemonEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // Act
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Assert
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        val successResult = result as RemoteMediator.MediatorResult.Success
        assertFalse(successResult.endOfPaginationReached)
        
        coVerify { pokemonDao.clearAll() }
        coVerify { pokemonDao.insertAll(any()) }
    }

    @Test
    fun `refresh load returns Success and endOfPaginationReached when no next page`() = runTest {
        // Arrange
        val response = PokemonResponse(
            count = 2,
            next = null,
            previous = null,
            results = listOf(PokemonDto(name = "bulbasaur", url = "url1"))
        )

        coEvery { apiService.getPokemonList(any(), any()) } returns response
        coEvery { pokemonDao.clearAll() } returns Unit
        coEvery { pokemonDao.insertAll(any()) } returns Unit

        val pagingState = PagingState<Int, PokemonEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // Act
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Assert
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        val successResult = result as RemoteMediator.MediatorResult.Success
        assertTrue(successResult.endOfPaginationReached)
    }

    @Test
    fun `refresh load returns Error when exception occurs`() = runTest {
        // Arrange
        coEvery { apiService.getPokemonList(any(), any()) } throws Exception("Network Error")

        val pagingState = PagingState<Int, PokemonEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // Act
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Assert
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}
