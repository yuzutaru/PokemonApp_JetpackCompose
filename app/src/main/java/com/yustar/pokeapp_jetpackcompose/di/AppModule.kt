package com.yustar.pokeapp_jetpackcompose.di

import com.yustar.pokeapp_jetpackcompose.data.repository.UserRepository
import com.yustar.pokeapp_jetpackcompose.data.repository.UserRepositoryImpl
import com.yustar.pokeapp_jetpackcompose.domain.LoginUserUseCase
import com.yustar.pokeapp_jetpackcompose.domain.RegisterUserUseCase
import com.yustar.pokeapp_jetpackcompose.domain.UpdateUserProfileUseCase
import org.koin.dsl.module

/**
 * Created by Yustar Pramudana on 05/03/26.
 */

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { RegisterUserUseCase(get()) }
    factory { LoginUserUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }
}

val appModule = listOf(dbModule, repositoryModule, useCaseModule)