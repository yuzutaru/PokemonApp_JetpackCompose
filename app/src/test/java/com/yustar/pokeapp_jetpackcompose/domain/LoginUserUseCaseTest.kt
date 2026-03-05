package com.yustar.pokeapp_jetpackcompose.domain

import com.yustar.pokeapp_jetpackcompose.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUserUseCaseTest {

    private val repository: UserRepository = mockk()
    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setUp() {
        loginUserUseCase = LoginUserUseCase(repository)
    }

    @Test
    fun `when login is called with correct credentials, return true`() = runTest { // Completed runTest
        // Given
        val username = "testUser"
        val password = "password123"
        coEvery { repository.login(username, password) } returns true

        // When
        val result = loginUserUseCase(username, password)

        // Then
        assertTrue(result)
        coVerify { repository.login(username, password) }
    }

    @Test
    fun `when login is called with wrong credentials, return false`() = runTest {
        // Given
        coEvery { repository.login(any(), any()) } returns false

        // When
        val result = loginUserUseCase("wrong", "wrong")

        // Then
        assertFalse(result)
    }
}