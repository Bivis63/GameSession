package com.example.gamesession.authentication.data.datasource

import com.example.gamesession.authentication.data.database.UserDao
import com.example.gamesession.authentication.data.database.UserMapper
import com.example.gamesession.authentication.domain.model.RuleSettings
import com.example.gamesession.authentication.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserDataSource(
    private val userDao: UserDao
) {

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            UserMapper.toDomainList(entities)
        }
    }

    suspend fun getUserByLoginAndPassword(login: String, password: String): User? {
        val entity = userDao.getUserByLoginAndPassword(login, password)
        return entity?.let { UserMapper.toDomain(entity) }
    }

    suspend fun insertUser(user: User) {
        val entity = UserMapper.toEntity(user)
        userDao.insertUser(entity)
    }

    suspend fun updateUser(user: User) {
        val entity = UserMapper.toEntity(user)
        userDao.updateUser(entity)
    }

    suspend fun deleteUser(user: User) {
        val entity = UserMapper.toEntity(user)
        userDao.deleteUser(entity)
    }

    suspend fun getCurrentUser(): User? {
        val entity = userDao.getCurrentUser()
        return entity?.let { UserMapper.toDomain(entity) }
    }

    suspend fun setUserAsCurrent(userId: Int) {
        userDao.setUserAsCurrent(userId)
    }

    suspend fun initializeSampleData(){

        val existingUsers = userDao.getAllUsers().first()
        if (existingUsers.isEmpty()) {
            userDao.insertUser(
                UserMapper.toEntity(
                    User(
                        login = "админ",
                        password = "admin123",
                        nickName = "Bivis",
                        phoneNumber = "+79270624520",
                        rule = RuleSettings.ADMIN,
                        isBlocked = false
                    )
                )
            )
            userDao.insertUser(
                UserMapper.toEntity(
                    User(
                        login = "юзер",
                        password = "user123",
                        nickName = "Butt-Head",
                        phoneNumber = "79277664541",
                        rule = RuleSettings.USER,
                        isBlocked = false
                    )
                )
            )
        }
    }

    suspend fun isNickNameExists(nickName: String, excludeUserId: Int): Boolean {
        return userDao.isNickNameExists(nickName, excludeUserId) > 0
    }

    suspend fun isPhoneNumberExists(phoneNumber: String, excludeUserId: Int): Boolean {
        return userDao.isPhoneNumberExists(phoneNumber, excludeUserId) > 0
    }
}

