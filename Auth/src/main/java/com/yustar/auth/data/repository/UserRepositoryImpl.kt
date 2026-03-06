package com.yustar.auth.data.repository

import com.yustar.auth.data.local.User
import com.yustar.auth.data.local.UserDao

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        address: String,
        phoneNumber: String
    ) {
        val user = User(
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phoneNumber = phoneNumber
        )
        userDao.insertUser(user)
    }

    override suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return user?.password == password
    }

    override suspend fun getUser(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    override suspend fun updateUserProfile(
        username: String,
        firstName: String?,
        lastName: String?,
        address: String?,
        phoneNumber: String?
    ) {
        userDao.updateUserProfile(username, firstName, lastName, address, phoneNumber)
    }
}