package com.yustar.dashboard.presentation.di

import com.yustar.dashboard.presentation.viewmodel.DashboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

val dashboardModule = module {
    viewModel { DashboardViewModel() }
}