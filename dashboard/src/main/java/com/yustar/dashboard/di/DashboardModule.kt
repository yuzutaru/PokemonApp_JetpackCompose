package com.yustar.dashboard.di

import androidx.room.Room
import com.yustar.dashboard.data.local.PokemonDatabase
import com.yustar.dashboard.data.remote.network.PokemonApiService
import com.yustar.dashboard.data.repository.PokemonRepository
import com.yustar.dashboard.domain.GetPokemonDetailUseCase
import com.yustar.dashboard.domain.GetPokemonUseCase
import com.yustar.dashboard.domain.GetUserUseCase
import com.yustar.dashboard.domain.UpdateUserProfileUseCase
import com.yustar.dashboard.presentation.viewmodel.DashboardViewModel
import com.yustar.dashboard.presentation.viewmodel.DetailViewModel
import com.yustar.dashboard.presentation.viewmodel.MenuViewModel
import com.yustar.dashboard.presentation.viewmodel.ProfileViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

val dashboardModule = module {
    // Network
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    // Api
    single { get<Retrofit>().create(PokemonApiService::class.java) }

    // Repository
    single { PokemonRepository(get(), get()) }

    // Database
    single {
        Room.databaseBuilder(get(), PokemonDatabase::class.java, "poke_db").build()
    }
    single { get<PokemonDatabase>().pokemonDao() }

    //UseCase
    factory { GetUserUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }
    factory { GetPokemonUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }

    //ViewModel
    viewModel { DashboardViewModel() }
    viewModel { MenuViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { DetailViewModel(get()) }
}
