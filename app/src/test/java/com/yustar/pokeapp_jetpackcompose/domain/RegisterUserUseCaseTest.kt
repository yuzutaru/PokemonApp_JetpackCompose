package com.yustar.pokeapp_jetpackcompose.domain

import com.yustar.pokeapp_jetpackcompose.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RegisterUserUseCaseTest {

    private val repository: UserRepository = mockk()
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Before
    fun setUp() {
        registerUserUseCase = RegisterUserUseCase(repository)
    }

    @Test
    fun `when invoke is called, it should call repository register`() = runTest {
        // Given
        val username = "testUser"
        val password = "password123"
        coEvery { repository.register(username, password) } just runs

        // When
        registerUserUseCase(username, password)

        // Then
        coVerify { repository.register(username, password) }
    }
}
