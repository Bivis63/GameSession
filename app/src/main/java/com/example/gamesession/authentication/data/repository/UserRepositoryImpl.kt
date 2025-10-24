package com.example.gamesession.authentication.data.repository

import com.example.gamesession.authentication.data.datasource.UserDataSource
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return userDataSource.getAllUsers()
    }

    override suspend fun getUserByLoginAndPassword(
        login: String,
        password: String
    ): User? {
        return userDataSource.getUserByLoginAndPassword(login,password)
    }

    override suspend fun insertUser(user: User) {
        userDataSource.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDataSource.updateUser(user)
    }

    override suspend fun deleteUser(user: User) {
        userDataSource.deleteUser(user)
    }

    override suspend fun initializeSampleData() {
        userDataSource.initializeSampleData()
    }

    override suspend fun isNickNameExists(nickName: String, excludeUserId: Int): Boolean {
        return userDataSource.isNickNameExists(nickName, excludeUserId)
    }

    override suspend fun isPhoneNumberExists(phoneNumber: String, excludeUserId: Int): Boolean {
        return userDataSource.isPhoneNumberExists(phoneNumber, excludeUserId)
    }

    override suspend fun getCurrentUser(): User? {
        return userDataSource.getCurrentUser()
    }

    override suspend fun setUserAsCurrent(userId: Int) {
        userDataSource.setUserAsCurrent(userId)
    }
}