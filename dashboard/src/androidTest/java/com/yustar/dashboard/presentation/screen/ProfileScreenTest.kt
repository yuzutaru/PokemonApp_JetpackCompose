package com.yustar.dashboard.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.yustar.dashboard.R
import com.yustar.dashboard.presentation.event.ProfileUiEvent
import com.yustar.dashboard.presentation.state.ProfileUiState
import com.yustar.dashboard.presentation.viewmodel.ProfileViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for ProfileScreen.
 */
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun profileContent_displaysAllFields() {
        composeTestRule.setContent {
            ProfileContent(
                paddingValues = PaddingValues(),
                uiState = ProfileUiState()
            )
        }

        // Check if labels are displayed
        composeTestRule.onNodeWithText(context.getString(R.string.update_profile)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.first_name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.last_name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.address)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.phone_number)).assertIsDisplayed()
        
        // Check if placeholders are displayed (since values are empty)
        composeTestRule.onNodeWithText(context.getString(R.string.input_first_name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.input_last_name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.input_address)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.input_phone_number)).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.save)).assertIsDisplayed()
    }

    @Test
    fun profileContent_enteringText_callsCallbacks() {
        var firstName = ""
        var lastName = ""
        var address = ""
        var phoneNumber = ""

        composeTestRule.setContent {
            ProfileContent(
                paddingValues = PaddingValues(),
                uiState = ProfileUiState(),
                onFirstNameChanged = { firstName = it },
                onLastNameChanged = { lastName = it },
                onAddressChanged = { address = it },
                onPhoneNumberChanged = { phoneNumber = it }
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.input_first_name)).performTextInput("John")
        assert(firstName == "John")

        composeTestRule.onNodeWithText(context.getString(R.string.input_last_name)).performTextInput("Doe")
        assert(lastName == "Doe")

        composeTestRule.onNodeWithText(context.getString(R.string.input_address)).performTextInput("123 Street")
        assert(address == "123 Street")

        composeTestRule.onNodeWithText(context.getString(R.string.input_phone_number)).performTextInput("08123456789")
        assert(phoneNumber == "08123456789")
    }

    @Test
    fun profileContent_saveClick_callsCallback() {
        var saveClicked = false
        composeTestRule.setContent {
            ProfileContent(
                paddingValues = PaddingValues(),
                uiState = ProfileUiState(),
                onSaveClick = { saveClicked = true }
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.save)).performClick()
        assert(saveClicked)
    }

    @Test
    fun profileContent_loadingState_hidesSaveText() {
        composeTestRule.setContent {
            ProfileContent(
                paddingValues = PaddingValues(),
                uiState = ProfileUiState(isLoading = true)
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.save)).assertDoesNotExist()
    }

    @Test
    fun profileScreen_successState_triggersResetSuccess() {
        val viewModel = mockk<ProfileViewModel>(relaxed = true)
        val uiStateFlow = MutableStateFlow(ProfileUiState(isSuccess = true))
        
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            ProfileScreen(
                paddingValues = PaddingValues(),
                viewModel = viewModel
            )
        }

        // Verify that ResetSuccess event was sent to ViewModel
        verify { viewModel.onEvent(ProfileUiEvent.ResetSuccess) }
    }

    @Test
    fun profileScreen_errorState_triggersClearError() {
        val viewModel = mockk<ProfileViewModel>(relaxed = true)
        val uiStateFlow = MutableStateFlow(ProfileUiState(error = "Test Error"))
        
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            ProfileScreen(
                paddingValues = PaddingValues(),
                viewModel = viewModel
            )
        }

        // Verify that ClearError event was sent to ViewModel
        verify { viewModel.onEvent(ProfileUiEvent.ClearError) }
    }
}
