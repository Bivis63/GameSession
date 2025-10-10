package com.example.gamesession.authentication.domain.repository

import com.example.gamesession.authentication.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsers():Flow<List<User>>
    suspend fun getUserByLoginAndPassword(login: String, password: String): User?
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user:User)
    suspend fun initializeSampleData()
}