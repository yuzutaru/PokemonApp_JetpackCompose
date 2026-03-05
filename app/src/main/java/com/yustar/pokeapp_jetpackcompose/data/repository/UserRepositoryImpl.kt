package com.yustar.pokeapp_jetpackcompose.data.repository

import com.yustar.pokeapp_jetpackcompose.data.local.User
import com.yustar.pokeapp_jetpackcompose.data.local.UserDao

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun register(username: String, password: String) {
        val user = User(username, password)
        userDao.insertUser(user)
    }

    override suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return user?.password == password
    }
}