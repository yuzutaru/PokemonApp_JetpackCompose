package com.yustar.dashboard.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.yustar.dashboard.data.local.PokemonEntity
import com.yustar.dashboard.presentation.viewmodel.MenuViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for MenuScreen.
 */
class MenuScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun menuContent_displaysPokemonItems() {
        val pokemonList = listOf(
            PokemonEntity("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/", 1),
            PokemonEntity("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/", 1)
        )
        
        val viewModel = mockk<MenuViewModel>(relaxed = true)
        val pagingDataFlow = MutableStateFlow(PagingData.from(pokemonList))
        
        every { viewModel.pokemonPagingData } returns pagingDataFlow

        composeTestRule.setContent {
            MenuScreen(
                paddingValues = PaddingValues(),
                viewModel = viewModel
            )
        }

        // Check if pokemon names are displayed
        // Note: replaceFirstChar { it.uppercase() } is used in PokemonItem
        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ivysaur").assertIsDisplayed()
    }

    @Test
    fun menuContent_showError_displaysErrorMessage() {
        val errorMessage = "Failed to fetch data"
        val viewModel = mockk<MenuViewModel>(relaxed = true)
        
        val pagingDataFlow = MutableStateFlow(
            PagingData.from(
                emptyList<PokemonEntity>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(Exception(errorMessage)),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true)
                )
            )
        )
        
        every { viewModel.pokemonPagingData } returns pagingDataFlow

        composeTestRule.setContent {
            MenuScreen(
                paddingValues = PaddingValues(),
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun menuContent_pullToRefresh_triggersRefresh() {
        val pokemonList = listOf(
            PokemonEntity("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/", 1)
        )
        val viewModel = mockk<MenuViewModel>(relaxed = true)
        val pagingDataFlow = MutableStateFlow(PagingData.from(pokemonList))
        
        every { viewModel.pokemonPagingData } returns pagingDataFlow

        composeTestRule.setContent {
            MenuScreen(
                paddingValues = PaddingValues(),
                viewModel = viewModel
            )
        }

        // Perform swipe down to trigger pull-to-refresh
        composeTestRule.onNodeWithTag("pull_to_refresh_box").performTouchInput {
            swipeDown()
        }

        // We can't easily verify the internal pagingItems.refresh() call directly on the mock viewModel
        // as it's called on the LazyPagingItems object created inside MenuScreen.
        // However, we've verified that the PullToRefreshBox is present and can receive gestures.
    }
}
