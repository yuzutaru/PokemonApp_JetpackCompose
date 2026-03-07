package com.yustar.auth.di

import androidx.room.Room
import com.yustar.auth.data.local.UserDB
import com.yustar.auth.data.repository.UserRepository
import com.yustar.auth.data.repository.UserRepositoryImpl
import com.yustar.auth.domain.LoginUserUseCase
import com.yustar.auth.domain.LoginUserUseCaseImpl
import com.yustar.auth.domain.RegisterUserUseCase
import com.yustar.auth.presentation.LoginViewModel
import com.yustar.auth.session.SessionManager
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Yustar Pramudana on 06/03/26.
 */

val authModule = module {
    // Room
    single {
        Room.databaseBuilder(
            get(),
            UserDB::class.java,
            "app_db"
        ).build()
    }

    single { get<UserDB>().userDao() }

    // Repository
    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    // Session
    single { SessionManager(get()) }

    // UseCases
    factory<LoginUserUseCase> { LoginUserUseCaseImpl(get(), get()) }
    factory { RegisterUserUseCase(get()) }

    //ViewModel
    viewModel { LoginViewModel(get()) }
}