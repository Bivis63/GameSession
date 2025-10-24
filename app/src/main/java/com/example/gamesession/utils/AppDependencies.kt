package com.example.gamesession.utils

import android.content.Context
import com.example.gamesession.authentication.data.database.UsersDataBase
import com.example.gamesession.authentication.data.datasource.UserDataSource
import com.example.gamesession.authentication.data.datasource.ComputerDataSource
import com.example.gamesession.authentication.data.datasource.GameSessionDataSource
import com.example.gamesession.authentication.data.datasource.SessionTariffDataSource
import com.example.gamesession.authentication.data.repository.UserRepositoryImpl
import com.example.gamesession.authentication.data.repository.ComputerRepositoryImpl
import com.example.gamesession.authentication.data.repository.GameSessionRepositoryImpl
import com.example.gamesession.authentication.data.repository.SessionTariffRepositoryImpl
import com.example.gamesession.authentication.domain.repository.UserRepository
import com.example.gamesession.authentication.domain.repository.ComputerRepository
import com.example.gamesession.authentication.domain.repository.GameSessionRepository
import com.example.gamesession.authentication.domain.repository.SessionTariffRepository
import com.example.gamesession.authentication.domain.usecase.AuthenticateUserUseCase
import com.example.gamesession.authentication.domain.usecase.DeleteUserUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllUsersUseCase
import com.example.gamesession.authentication.domain.usecase.GetCurrentUserUseCase
import com.example.gamesession.authentication.domain.usecase.InitializeUsersDataUseCase
import com.example.gamesession.authentication.domain.usecase.InsertUserUseCase
import com.example.gamesession.authentication.domain.usecase.SetCurrentUserUseCase
import com.example.gamesession.authentication.domain.usecase.UpdateUserUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllComputersUseCase
import com.example.gamesession.authentication.domain.usecase.GetAvailableComputersUseCase
import com.example.gamesession.authentication.domain.usecase.InsertComputerUseCase
import com.example.gamesession.authentication.domain.usecase.DeleteComputerUseCase
import com.example.gamesession.authentication.domain.usecase.GetNextComputerCodeUseCase
import com.example.gamesession.authentication.domain.usecase.InitializeComputersDataUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllGameSessionsUseCase
import com.example.gamesession.authentication.domain.usecase.InsertGameSessionUseCase
import com.example.gamesession.authentication.domain.usecase.GetActiveTariffsUseCase
import com.example.gamesession.authentication.domain.usecase.InitializeTariffsDataUseCase
import com.example.gamesession.authentication.domain.usecase.CheckComputerAvailabilityUseCase
import com.example.gamesession.authentication.domain.usecase.GetAvailableTimeSlotsUseCase
import com.example.gamesession.authentication.domain.usecase.StartSessionUseCase
import com.example.gamesession.authentication.domain.usecase.PauseSessionUseCase
import com.example.gamesession.authentication.domain.usecase.ResumeSessionUseCase
import com.example.gamesession.authentication.domain.usecase.FinishSessionUseCase

object AppDependencies {

    private var database: UsersDataBase? = null
    private var userRepository: UserRepository? = null
    private var computerRepository: ComputerRepository? = null
    private var gameSessionRepository: GameSessionRepository? = null
    private var sessionTariffRepository: SessionTariffRepository? = null

    fun initialize(context: Context) {
        if (database == null || userRepository == null || computerRepository == null || gameSessionRepository == null || sessionTariffRepository == null) {
            synchronized(this) {
                if (database == null || userRepository == null || computerRepository == null || gameSessionRepository == null || sessionTariffRepository == null) {
                    val db = UsersDataBase.getDataBase(context)
                    database = db


                    val userDao = db.userDao()
                    val userDataSource = UserDataSource(userDao)
                    userRepository = UserRepositoryImpl(userDataSource)

                    val computerDao = db.computerDao()
                    val computerDataSource = ComputerDataSource(computerDao)
                    computerRepository = ComputerRepositoryImpl(computerDataSource)

                    val sessionTariffDao = db.sessionTariffDao()
                    val sessionTariffDataSource = SessionTariffDataSource(sessionTariffDao)
                    sessionTariffRepository = SessionTariffRepositoryImpl(sessionTariffDataSource)

                    val gameSessionDao = db.gameSessionDao()
                    val gameSessionDataSource = GameSessionDataSource(gameSessionDao)
                    gameSessionRepository = GameSessionRepositoryImpl(gameSessionDataSource, sessionTariffDataSource)
                }
            }
        }
    }

    fun getUserRepository(): UserRepository =
        userRepository ?: throw IllegalStateException("AppDependencies not initialized")

    fun getComputerRepository(): ComputerRepository =
        computerRepository ?: throw IllegalStateException("AppDependencies not initialized")

    fun getGameSessionRepository(): GameSessionRepository =
        gameSessionRepository ?: throw IllegalStateException("AppDependencies not initialized")

    fun getSessionTariffRepository(): SessionTariffRepository =
        sessionTariffRepository ?: throw IllegalStateException("AppDependencies not initialized")

    val getAllUsersUseCase: GetAllUsersUseCase by lazy { GetAllUsersUseCase(getUserRepository()) }
    val getAllComputersUseCase: GetAllComputersUseCase by lazy { GetAllComputersUseCase(getComputerRepository()) }
    val getAvailableComputersUseCase: GetAvailableComputersUseCase by lazy { GetAvailableComputersUseCase(getComputerRepository()) }
    val insertComputerUseCase: InsertComputerUseCase by lazy { InsertComputerUseCase(getComputerRepository()) }
    val deleteComputerUseCase: DeleteComputerUseCase by lazy { DeleteComputerUseCase(getComputerRepository()) }
    val getNextComputerCodeUseCase: GetNextComputerCodeUseCase by lazy { GetNextComputerCodeUseCase(getComputerRepository()) }
    val initializeComputersDataUseCase: InitializeComputersDataUseCase by lazy { InitializeComputersDataUseCase(getComputerRepository()) }
    val getAllGameSessionsUseCase: GetAllGameSessionsUseCase by lazy { GetAllGameSessionsUseCase(getGameSessionRepository()) }
    val insertGameSessionUseCase: InsertGameSessionUseCase by lazy { InsertGameSessionUseCase(getGameSessionRepository()) }
    val getActiveTariffsUseCase: GetActiveTariffsUseCase by lazy { GetActiveTariffsUseCase(getSessionTariffRepository()) }
    val initializeTariffsDataUseCase: InitializeTariffsDataUseCase by lazy { InitializeTariffsDataUseCase(getSessionTariffRepository()) }
    val checkComputerAvailabilityUseCase: CheckComputerAvailabilityUseCase by lazy { CheckComputerAvailabilityUseCase(getGameSessionRepository()) }
    val getAvailableTimeSlotsUseCase: GetAvailableTimeSlotsUseCase by lazy { GetAvailableTimeSlotsUseCase(getGameSessionRepository()) }
    val startSessionUseCase: StartSessionUseCase by lazy { StartSessionUseCase(getGameSessionRepository()) }
    val pauseSessionUseCase: PauseSessionUseCase by lazy { PauseSessionUseCase(getGameSessionRepository()) }
    val resumeSessionUseCase: ResumeSessionUseCase by lazy { ResumeSessionUseCase(getGameSessionRepository()) }
    val finishSessionUseCase: FinishSessionUseCase by lazy { FinishSessionUseCase(getGameSessionRepository()) }
    val authenticateUserUseCase: AuthenticateUserUseCase by lazy { AuthenticateUserUseCase(getUserRepository()) }
    val insertUserUseCase: InsertUserUseCase by lazy { InsertUserUseCase(getUserRepository()) }
    val updateUserUseCase: UpdateUserUseCase by lazy { UpdateUserUseCase(getUserRepository()) }
    val deleteUserUseCase: DeleteUserUseCase by lazy { DeleteUserUseCase(getUserRepository()) }
    val initializeUsersDataUseCase: InitializeUsersDataUseCase by lazy { InitializeUsersDataUseCase(getUserRepository()) }
    val getCurrentUserUseCase: GetCurrentUserUseCase by lazy { GetCurrentUserUseCase(getUserRepository()) }
    val setCurrentUserUseCase: SetCurrentUserUseCase by lazy { SetCurrentUserUseCase(getUserRepository()) }
}