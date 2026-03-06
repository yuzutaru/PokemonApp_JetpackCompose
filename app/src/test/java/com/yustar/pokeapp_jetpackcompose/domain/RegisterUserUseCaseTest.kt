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
        val firstName = "John"
        val lastName = "Doe"
        val address = "123 Main St"
        val phoneNumber = "555-1234"
        
        coEvery { 
            repository.register(username, password, firstName, lastName, address, phoneNumber) 
        } just runs

        // When
        registerUserUseCase(username, password, firstName, lastName, address, phoneNumber)

        // Then
        coVerify { 
            repository.register(username, password, firstName, lastName, address, phoneNumber) 
        }
    }

    @Test
    fun `when invoke is called with only username and password, it should call repository register with default empty strings`() = runTest {
        // Given
        val username = "testUser"
        val password = "password123"
        
        coEvery { 
            repository.register(username, password, "", "", "", "") 
        } just runs

        // When
        registerUserUseCase(username, password)

        // Then
        coVerify { 
            repository.register(username, password, "", "", "", "") 
        }
    }
}