package com.example.gamesession.authentication.presentation.user

import com.arkivanov.decompose.ComponentContext
import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.model.SessionStatus
import com.example.gamesession.authentication.domain.usecase.GetAllComputersUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllGameSessionsUseCase
import com.example.gamesession.authentication.domain.usecase.UpdateUserUseCase
import com.example.gamesession.authentication.domain.usecase.FinishSessionUseCase
import com.example.gamesession.utils.SessionUtils
import kotlinx.coroutines.delay
import com.example.gamesession.utils.AppDependencies
import com.example.gamesession.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DefaultUserComponent(
    componentContext: ComponentContext,
    private val user: User,
    private val onLogoutCallback: () -> Unit,
    private val updateUserUseCase: UpdateUserUseCase = AppDependencies.updateUserUseCase,
    private val getAllGameSessionsUseCase: GetAllGameSessionsUseCase = AppDependencies.getAllGameSessionsUseCase,
    private val getAllComputersUseCase: GetAllComputersUseCase = AppDependencies.getAllComputersUseCase,
    private val finishSessionUseCase: FinishSessionUseCase = AppDependencies.finishSessionUseCase
) : UserComponent, ComponentContext by componentContext {

    companion object {
        private const val KEY = "UserComponent"
    }

    private val scope = componentScope()

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = UserComponent.Model.serializer())
            ?: UserComponent.Model(currentUser = user)
    )

    init {
        stateKeeper.register(KEY, strategy = UserComponent.Model.serializer()) { model.value }
        loadUserSessions()
        startTimeUpdater()
    }
    
    private fun loadUserSessions() {
        scope.launch {
            val gameSessions = getAllGameSessionsUseCase().first()
            val computers = getAllComputersUseCase().first()
            
            val sortedSessions = gameSessions
                .filter { it.userId == user.id }
                .sortedWith(compareBy<GameSession> { session ->
                    val dateParts = session.date.split(".")
                    if (dateParts.size == 3) {
                        "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}"
                    } else {
                        session.date
                    }
                }.thenBy { session ->
                    session.time
                })
            
            val userSessions = sortedSessions.map { session ->
                val computer = computers.find { it.id == session.computerId }
                val actualDuration = SessionUtils.calculateActualDurationMinutes(session)
                UserComponent.UserSessionItem(
                    id = session.id,
                    date = session.date,
                    time = session.time,
                    duration = session.durationHours,
                    computerName = computer?.name ?: "Неизвестный ПК",
                    price = session.totalCost,
                    status = session.status,
                    actualDuration = SessionUtils.formatDuration(actualDuration),
                    startTime = session.startTime,
                    durationHours = session.durationHours
                )
            }
            
            _model.value = _model.value.copy(userSessions = userSessions)
        }
    }

    private fun startTimeUpdater() {
        scope.launch {
            while (true) {
                delay(1000)
                val hasActiveSessions = _model.value.userSessions.any { 
                    it.status == SessionStatus.RUNNING || it.status == SessionStatus.PAUSED 
                }
                if (hasActiveSessions) {
                    loadUserSessions()
                    checkAndAutoFinishExpiredUserSessions()
                }
            }
        }
    }

    private suspend fun checkAndAutoFinishExpiredUserSessions() {
        val allSessions = getAllGameSessionsUseCase().first()
        val userRunningSessions = allSessions.filter { session ->
            session.userId == user.id && session.status == SessionStatus.RUNNING
        }
        for (session in userRunningSessions) {
            if (SessionUtils.isSessionTimeExpired(session)) {
                finishSessionUseCase(session.id)
                    .onSuccess { cost ->
                        loadUserSessions()
                    }
                    .onFailure { error ->
                        println("Ошибка автоматического завершения сессии пользователя ${session.id}: ${error.message}")
                    }
            }
        }
    }

    override val model: StateFlow<UserComponent.Model>
        get() = _model.asStateFlow()

    override fun onLogout() {
        scope.launch {
            val updatedUser = user.copy(status = false)
            updateUserUseCase(updatedUser)
        }
        onLogoutCallback()
    }
}




