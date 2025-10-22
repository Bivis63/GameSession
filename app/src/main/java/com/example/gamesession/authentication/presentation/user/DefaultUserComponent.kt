package com.example.gamesession.authentication.presentation.user

import com.arkivanov.decompose.ComponentContext
import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.usecase.GetAllComputersUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllGameSessionsUseCase
import com.example.gamesession.authentication.domain.usecase.UpdateUserUseCase
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
    private val getAllComputersUseCase: GetAllComputersUseCase = AppDependencies.getAllComputersUseCase
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
                UserComponent.UserSessionItem(
                    id = session.id,
                    date = session.date,
                    time = session.time,
                    duration = session.durationHours,
                    computerName = computer?.name ?: "Неизвестный ПК",
                    price = 0
                )
            }
            
            _model.value = _model.value.copy(userSessions = userSessions)
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




