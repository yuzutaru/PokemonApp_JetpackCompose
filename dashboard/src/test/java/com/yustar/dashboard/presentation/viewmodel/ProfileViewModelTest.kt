package com.yustar.dashboard.presentation.viewmodel

import com.yustar.core.data.local.User
import com.yustar.core.session.SessionManager
import com.yustar.dashboard.domain.GetUserUseCase
import com.yustar.dashboard.domain.UpdateUserProfileUseCase
import com.yustar.dashboard.presentation.event.ProfileUiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val sessionManager: SessionManager = mockk()
    private val getUserUseCase: GetUserUseCase = mockk()
    private val updateUserProfileUseCase: UpdateUserProfileUseCase = mockk()

    private lateinit var viewModel: ProfileViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val testUser = User(
        username = "testuser",
        password = "password",
        firstName = "John",
        lastName = "Doe",
        address = "123 Street",
        phoneNumber = "123456789"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = ProfileViewModel(sessionManager, getUserUseCase, updateUserProfileUseCase)
    }

    @Test
    fun `initialization loads user profile successfully`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser

        // When
        createViewModel()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(testUser.firstName, state.firstName)
        assertEquals(testUser.lastName, state.lastName)
        assertEquals(testUser.address, state.address)
        assertEquals(testUser.phoneNumber, state.phoneNumber)
        assertEquals("", state.error)
    }

    @Test
    fun `initialization fails when session is expired`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(null)

        // When
        createViewModel()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Session expired", state.error)
    }

    @Test
    fun `initialization fails when user is not found`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns null

        // When
        createViewModel()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("User not found", state.error)
    }

    @Test
    fun `on FirstNameChanged updates uiState`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        // When
        val newName = "Jane"
        viewModel.onEvent(ProfileUiEvent.OnFirstNameChanged(newName))

        // Then
        assertEquals(newName, viewModel.uiState.value.firstName)
    }

    @Test
    fun `on LastNameChanged updates uiState`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        // When
        val newLastName = "Smith"
        viewModel.onEvent(ProfileUiEvent.OnLastNameChanged(newLastName))

        // Then
        assertEquals(newLastName, viewModel.uiState.value.lastName)
    }

    @Test
    fun `on AddressChanged updates uiState`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        // When
        val newAddress = "456 Avenue"
        viewModel.onEvent(ProfileUiEvent.OnAddressChanged(newAddress))

        // Then
        assertEquals(newAddress, viewModel.uiState.value.address)
    }

    @Test
    fun `on PhoneNumberChanged updates uiState`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        // When
        val newPhone = "987654321"
        viewModel.onEvent(ProfileUiEvent.OnPhoneNumberChanged(newPhone))

        // Then
        assertEquals(newPhone, viewModel.uiState.value.phoneNumber)
    }

    @Test
    fun `on ClearError resets error state`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(null)
        createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.error.isNotEmpty())

        // When
        viewModel.onEvent(ProfileUiEvent.ClearError)

        // Then
        assertEquals("", viewModel.uiState.value.error)
    }

    @Test
    fun `on SaveClick saves profile successfully`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        val updatedFirstName = "Jane"
        viewModel.onEvent(ProfileUiEvent.OnFirstNameChanged(updatedFirstName))
        
        coEvery { 
            updateUserProfileUseCase(
                username = testUser.username,
                firstName = updatedFirstName,
                lastName = testUser.lastName,
                address = testUser.address,
                phoneNumber = testUser.phoneNumber
            )
        } returns Unit

        // When
        viewModel.onEvent(ProfileUiEvent.OnSaveClick)
        advanceUntilIdle()

        // Then
        coVerify { 
            updateUserProfileUseCase(
                username = testUser.username,
                firstName = updatedFirstName,
                lastName = testUser.lastName,
                address = testUser.address,
                phoneNumber = testUser.phoneNumber
            )
        }
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("", viewModel.uiState.value.error)
    }

    @Test
    fun `on SaveClick handles error`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        val errorMessage = "Failed to update"
        coEvery { 
            updateUserProfileUseCase(any(), any(), any(), any(), any())
        } throws Exception(errorMessage)

        // When
        viewModel.onEvent(ProfileUiEvent.OnSaveClick)
        advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `on SaveClick fails if session is expired`() = runTest {
        // Given
        // Initial load succeeds
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        // Session expires before save
        every { sessionManager.loggedUser } returns flowOf(null)

        // When
        viewModel.onEvent(ProfileUiEvent.OnSaveClick)
        advanceUntilIdle()

        // Then
        assertEquals("Session expired", viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `on LogoutClick logs out successfully`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        coEvery { sessionManager.logout() } returns Unit

        // When
        viewModel.onEvent(ProfileUiEvent.OnLogoutClick)
        advanceUntilIdle()

        // Then
        coVerify { sessionManager.logout() }
        assertTrue(viewModel.uiState.value.isLoggedOut)
    }

    @Test
    fun `on ResetLogoutState resets logout state`() = runTest {
        // Given
        every { sessionManager.loggedUser } returns flowOf(testUser.username)
        coEvery { getUserUseCase(testUser.username) } returns testUser
        createViewModel()
        advanceUntilIdle()

        coEvery { sessionManager.logout() } returns Unit
        viewModel.onEvent(ProfileUiEvent.OnLogoutClick)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isLoggedOut)

        // When
        viewModel.onEvent(ProfileUiEvent.ResetLogoutState)

        // Then
        assertFalse(viewModel.uiState.value.isLoggedOut)
    }
}
