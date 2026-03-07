package com.yustar.auth.domain

import com.yustar.auth.data.repository.UserRepository
import com.yustar.auth.session.SessionManager

/**
 * Created by Yustar Pramudana on 06/03/26.
 */

interface LoginUserUseCase {
    suspend operator fun invoke(
        username: String,
        password: String
    ): Boolean
}

class LoginUserUseCaseImpl(
    private val repository: UserRepository,
    private val session: SessionManager
) : LoginUserUseCase {

    override suspend fun invoke(
        username: String,
        password: String
    ): Boolean {
        val success = repository.login(username, password)

        if (success) {
            session.login(username)
        }

        return success
    }
}