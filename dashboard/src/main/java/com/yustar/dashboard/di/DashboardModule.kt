package com.yustar.dashboard.di

import com.yustar.core.session.SessionManager
import com.yustar.dashboard.domain.GetUserUseCase
import com.yustar.dashboard.domain.UpdateUserProfileUseCase
import com.yustar.dashboard.presentation.viewmodel.DashboardViewModel
import com.yustar.dashboard.presentation.viewmodel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

val dashboardModule = module {
    //UseCase
    factory { GetUserUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }

    //ViewModel
    viewModel { DashboardViewModel() }
    viewModel { ProfileViewModel(get(), get(), get()) }
}
