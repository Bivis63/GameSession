package com.example.gamesession.authentication.presentation.login

import com.arkivanov.decompose.ComponentContext
import com.example.gamesession.authentication.domain.usecase.AuthenticateUserUseCase
import com.example.gamesession.authentication.domain.usecase.InitializeUsersDataUseCase
import com.example.gamesession.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val authenticateUser: AuthenticateUserUseCase,
    private val initializeUsersData: InitializeUsersDataUseCase
) : LoginComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = LoginComponent.Model.serializer())
            ?: LoginComponent.Model()
    )

    init {
        stateKeeper.register(KEY, strategy = LoginComponent.Model.serializer()) { model.value }
        scope.launch { initializeUsersData }
    }

    override val model: StateFlow<LoginComponent.Model>
        get() = _model.asStateFlow()

    override fun onLoginChanged(value: String) {
        _model.update { it.copy(login = value, loginFailed = false) }
    }

    override fun onPasswordChanged(value: String) {
        _model.update { it.copy(password = value) }
    }

    override fun onSubmit() {
        val current = _model.value
        if (!current.isButtonEnabled) return
        scope.launch {
            val user = authenticateUser(current.login, current.password)
            if (user == null) {
                _model.update { it.copy(loginFailed = true) }
                return@launch
            }
        }
    }

    companion object {
        const val KEY = "DefaultLoginComponent"
    }
}