package com.yustar.pokeapp_jetpackcompose.data.repository

interface UserRepository {
    suspend fun register(username: String, password: String)
    suspend fun login(username: String, password: String): Boolean
}