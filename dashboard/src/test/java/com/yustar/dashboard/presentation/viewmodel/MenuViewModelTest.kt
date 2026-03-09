package com.yustar.dashboard.presentation.viewmodel

import androidx.paging.PagingData
import com.yustar.dashboard.domain.GetPokemonUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Yustar Pramudana on 09/03/26.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest {

    private val getPokemonUseCase: GetPokemonUseCase = mockk()
    private lateinit var viewModel: MenuViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getPokemonUseCase() } returns flowOf(PagingData.empty())
        viewModel = MenuViewModel(getPokemonUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals("", state.error)
    }

    @Test
    fun `when initialized, calls getPokemonUseCase`() = runTest {
        verify { getPokemonUseCase() }
    }
}
