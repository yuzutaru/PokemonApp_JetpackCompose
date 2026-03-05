package com.yustar.pokeapp_jetpackcompose

import android.app.Application
import com.yustar.pokeapp_jetpackcompose.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Yustar Pramudana on 05/03/26.
 */
class PokeApp_JetpackComposeApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PokeApp_JetpackComposeApplication)
            modules(appModule)
        }
    }
}