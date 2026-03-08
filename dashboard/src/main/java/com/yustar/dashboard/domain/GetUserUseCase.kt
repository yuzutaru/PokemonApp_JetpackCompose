package com.yustar.dashboard.domain

import com.yustar.core.data.local.User
import com.yustar.core.data.repository.UserRepository

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

class GetUserUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(username: String): User? {
        return repository.getUser(username)
    }
}
