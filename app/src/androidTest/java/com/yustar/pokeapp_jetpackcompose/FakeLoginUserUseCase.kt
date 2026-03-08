package com.yustar.pokeapp_jetpackcompose

import com.yustar.auth.domain.LoginUserUseCase
import io.mockk.mockk

class FakeLoginUserUseCase : LoginUserUseCase(mockk(relaxed = true), mockk(relaxed = true)) {
    var shouldSucceed: Boolean = true
    override suspend fun invoke(username: String, password: String): Boolean = shouldSucceed
}
