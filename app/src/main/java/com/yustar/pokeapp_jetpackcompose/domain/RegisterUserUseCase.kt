package com.yustar.pokeapp_jetpackcompose.domain

import com.yustar.pokeapp_jetpackcompose.data.repository.UserRepository

class RegisterUserUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        username: String,
        password: String
    ) {
        repository.register(username, password)
    }
}