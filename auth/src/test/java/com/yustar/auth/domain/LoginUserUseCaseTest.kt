package com.yustar.auth.domain

import com.yustar.core.data.repository.UserRepository
import com.yustar.auth.session.SessionManager
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
    private val session: SessionManager = mockk(relaxed = true)
    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setUp() {
        loginUserUseCase = LoginUserUseCase(repository, session)
    }

    @Test
    fun `when login is called with correct credentials, return true and login session`() = runTest {
        // Given
        val username = "testUser"
        val password = "password123"
        coEvery { repository.login(username, password) } returns true

        // When
        val result = loginUserUseCase(username, password)

        // Then
        assertTrue(result)
        coVerify { repository.login(username, password) }
        coVerify { session.login(username) }
    }

    @Test
    fun `when login is called with wrong credentials, return false and do not login session`() = runTest {
        // Given
        val username = "wrong"
        val password = "wrong"
        coEvery { repository.login(username, password) } returns false

        // When
        val result = loginUserUseCase(username, password)

        // Then
        assertFalse(result)
        coVerify { repository.login(username, password) }
        coVerify(exactly = 0) { session.login(any()) }
    }
}
