package com.example.gamesession.utils

import android.content.Context
import com.example.gamesession.authentication.data.database.UsersDataBase
import com.example.gamesession.authentication.data.datasource.UserDataSource
import com.example.gamesession.authentication.data.repository.UserRepositoryImpl
import com.example.gamesession.authentication.domain.repository.UserRepository
import com.example.gamesession.authentication.domain.usecase.AuthenticateUserUseCase
import com.example.gamesession.authentication.domain.usecase.DeleteUserUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllUsersUseCase
import com.example.gamesession.authentication.domain.usecase.InitializeUsersDataUseCase
import com.example.gamesession.authentication.domain.usecase.InsertUserUseCase
import com.example.gamesession.authentication.domain.usecase.UpdateUserUseCase

object AppDependencies {

    private var database: UsersDataBase? = null
    private var userRepository: UserRepository? = null

    fun initialize(context: Context) {
        if (database == null || userRepository == null) {
            synchronized(this) {
                if (database == null || userRepository == null) {
                    val db = UsersDataBase.getDataBase(context)
                    database = db
                    val userDao = db.userDao()
                    val dataSource = UserDataSource(userDao)
                    userRepository = UserRepositoryImpl(dataSource)
                }
            }
        }
    }

    fun getUserRepository(): UserRepository =
        userRepository ?: throw IllegalStateException("AppDependencies not initialized")

    val getAllUsersUseCase: GetAllUsersUseCase by lazy { GetAllUsersUseCase(getUserRepository()) }
    val authenticateUserUseCase: AuthenticateUserUseCase by lazy { AuthenticateUserUseCase(getUserRepository()) }
    val insertUserUseCase: InsertUserUseCase by lazy { InsertUserUseCase(getUserRepository()) }
    val updateUserUseCase: UpdateUserUseCase by lazy { UpdateUserUseCase(getUserRepository()) }
    val deleteUserUseCase: DeleteUserUseCase by lazy { DeleteUserUseCase(getUserRepository()) }
    val initializeUsersDataUseCase: InitializeUsersDataUseCase by lazy { InitializeUsersDataUseCase(getUserRepository()) }
}