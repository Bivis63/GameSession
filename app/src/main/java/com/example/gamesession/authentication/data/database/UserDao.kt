package com.example.gamesession.authentication.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE login = :login AND password  = :password  LIMIT 1")
    suspend fun getUserByLoginAndPassword(login: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE status = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("UPDATE users SET status = 1 WHERE id = :userId")
    suspend fun setUserAsCurrent(userId: Int)

    @Query("SELECT COUNT(*) FROM users WHERE nickName = :nickName AND id != :excludeUserId")
    suspend fun isNickNameExists(nickName: String, excludeUserId: Int): Int

    @Query("SELECT COUNT(*) FROM users WHERE phoneNumber = :phoneNumber AND id != :excludeUserId")
    suspend fun isPhoneNumberExists(phoneNumber: String, excludeUserId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}