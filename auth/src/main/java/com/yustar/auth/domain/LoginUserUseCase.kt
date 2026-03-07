package com.yustar.auth.domain

import com.yustar.auth.data.repository.UserRepository
import com.yustar.auth.session.SessionManager

class LoginUserUseCase(
    private val repository: UserRepository,
    private val session: SessionManager
) {

    suspend operator fun invoke(
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