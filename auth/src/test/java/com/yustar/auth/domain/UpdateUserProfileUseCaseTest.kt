package com.yustar.auth.domain

import com.yustar.auth.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateUserProfileUseCaseTest {

    private val repository: UserRepository = mockk()
    private lateinit var updateUserProfileUseCase: UpdateUserProfileUseCase

    @Before
    fun setUp() {
        updateUserProfileUseCase = UpdateUserProfileUseCase(repository)
    }

    @Test
    fun `when invoke is called, verify repository updateUserProfile is called with correct parameters`() = runTest {
        // Given
        val username = "testUser"
        val firstName = "John"
        val lastName = "Doe"
        val address = "123 Street"
        val phoneNumber = "123456789"

        coEvery {
            repository.updateUserProfile(
                username,
                firstName,
                lastName,
                address,
                phoneNumber
            )
        } just runs

        // When
        updateUserProfileUseCase(
            username = username,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phoneNumber = phoneNumber
        )

        // Then
        coVerify {
            repository.updateUserProfile(
                username = username,
                firstName = firstName,
                lastName = lastName,
                address = address,
                phoneNumber = phoneNumber
            )
        }
    }

    @Test
    fun `when invoke is called with null values, verify repository updateUserProfile is called with nulls`() = runTest {
        // Given
        val username = "testUser"

        coEvery {
            repository.updateUserProfile(
                username = username,
                firstName = null,
                lastName = null,
                address = null,
                phoneNumber = null
            )
        } just runs

        // When
        updateUserProfileUseCase(username = username)

        // Then
        coVerify {
            repository.updateUserProfile(
                username = username,
                firstName = null,
                lastName = null,
                address = null,
                phoneNumber = null
            )
        }
    }
}
