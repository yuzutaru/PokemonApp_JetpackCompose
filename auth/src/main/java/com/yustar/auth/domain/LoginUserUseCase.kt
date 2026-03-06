package com.yustar.auth.domain

import com.yustar.auth.data.repository.UserRepository

class LoginUserUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): Boolean {
        return repository.login(username, password)
    }
}