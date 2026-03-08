package com.yustar.dashboard.domain

import com.yustar.core.data.local.User
import com.yustar.core.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

class GetUserUseCaseTest {

    private val repository: UserRepository = mockk()
    private lateinit var getUserUseCase: GetUserUseCase

    @Before
    fun setUp() {
        getUserUseCase = GetUserUseCase(repository)
    }

    @Test
    fun `when getUser is called, return user from repository`() = runTest {
        // Given
        val username = "testUser"
        val expectedUser = User(
            username = username,
            password = "password123",
            firstName = "John",
            lastName = "Doe",
            address = "123 Street",
            phoneNumber = "1234567890"
        )
        coEvery { repository.getUser(username) } returns expectedUser

        // When
        val result = getUserUseCase(username)

        // Then
        assertEquals(expectedUser, result)
        coVerify { repository.getUser(username) }
    }

    @Test
    fun `when getUser is called and user not found, return null`() = runTest {
        // Given
        val username = "nonExistentUser"
        coEvery { repository.getUser(username) } returns null

        // When
        val result = getUserUseCase(username)

        // Then
        assertNull(result)
        coVerify { repository.getUser(username) }
    }
}
