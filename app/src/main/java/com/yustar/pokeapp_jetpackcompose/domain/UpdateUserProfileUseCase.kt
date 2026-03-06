package com.yustar.pokeapp_jetpackcompose.domain

import com.yustar.pokeapp_jetpackcompose.data.repository.UserRepository

class UpdateUserProfileUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        username: String,
        firstName: String? = null,
        lastName: String? = null,
        address: String? = null,
        phoneNumber: String? = null
    ) {
        repository.updateUserProfile(
            username = username,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phoneNumber = phoneNumber
        )
    }
}