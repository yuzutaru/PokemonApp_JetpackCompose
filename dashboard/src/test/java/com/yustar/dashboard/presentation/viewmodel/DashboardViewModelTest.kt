package com.yustar.dashboard.presentation.viewmodel

import com.yustar.dashboard.presentation.event.DashboardUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DashboardViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(0, state.selectedTab)
        assertEquals(false, state.isLoading)
        assertEquals("", state.error)
    }

    @Test
    fun `on OnMenuSelected updates selectedTab`() = runTest {
        // Given
        val selectedIndex = 1

        // When
        viewModel.onEvent(DashboardUiEvent.OnMenuSelected(selectedIndex))

        // Then
        assertEquals(selectedIndex, viewModel.uiState.value.selectedTab)
    }
}
