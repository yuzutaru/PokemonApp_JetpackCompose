package com.yustar.auth.di

import com.yustar.auth.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Yustar Pramudana on 06/03/26.
 */

val authModule = module {
    viewModel { LoginViewModel() }
}