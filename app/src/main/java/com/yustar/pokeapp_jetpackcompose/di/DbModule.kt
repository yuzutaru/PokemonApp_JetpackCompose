package com.yustar.pokeapp_jetpackcompose.di

import android.app.Application
import androidx.room.Room
import com.yustar.pokeapp_jetpackcompose.data.local.UserDB
import com.yustar.pokeapp_jetpackcompose.data.local.UserDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Yustar Pramudana on 05/03/26.
 */

val dbModule = module {
    fun userDB(app: Application): UserDB {
        return Room.databaseBuilder(app, UserDB::class.java, "user.db").build()
    }

    fun provideUserDao(db: UserDB): UserDao {
        return db.userDao()
    }

    single { userDB(androidApplication()) }
    single { provideUserDao(get()) }
}