package com.yustar.auth.domain

import com.yustar.auth.data.repository.UserRepository

class RegisterUserUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        username: String,
        password: String,
        firstName: String = "",
        lastName: String = "",
        address: String = "",
        phoneNumber: String = ""
    ) {
        repository.register(
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phoneNumber = phoneNumber
        )
    }
}