package com.yustar.auth.presentation.viewmodel

import com.yustar.auth.domain.LoginUserUseCase
import com.yustar.auth.presentation.LoginViewModel
import com.yustar.auth.presentation.event.LoginUiEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val loginUseCase: LoginUserUseCase = mockk()
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when email changes, uiState should be updated`() {
        val email = "test@example.com"
        viewModel.onEvent(LoginUiEvent.OnEmailChanged(email))
        Assert.assertEquals(email, viewModel.uiState.value.email)
    }

    @Test
    fun `when password changes, uiState should be updated`() {
        val password = "password123"
        viewModel.onEvent(LoginUiEvent.OnPasswordChanged(password))
        Assert.assertEquals(password, viewModel.uiState.value.password)
    }

    @Test
    fun `when login is successful, onSuccess should be called`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        var successCalled = false

        viewModel.onEvent(LoginUiEvent.OnEmailChanged(email))
        viewModel.onEvent(LoginUiEvent.OnPasswordChanged(password))

        coEvery { loginUseCase(email, password) } returns true

        // When
        viewModel.login { successCalled = true }
        advanceUntilIdle()

        // Then
        Assert.assertTrue(successCalled)
        Assert.assertEquals("", viewModel.uiState.value.error)
    }

    @Test
    fun `when login fails, error state should be updated`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrong_password"

        viewModel.onEvent(LoginUiEvent.OnEmailChanged(email))
        viewModel.onEvent(LoginUiEvent.OnPasswordChanged(password))

        coEvery { loginUseCase(email, password) } returns false

        // When
        viewModel.login {}
        advanceUntilIdle()

        // Then
        Assert.assertEquals("Invalid credentials", viewModel.uiState.value.error)
    }
}