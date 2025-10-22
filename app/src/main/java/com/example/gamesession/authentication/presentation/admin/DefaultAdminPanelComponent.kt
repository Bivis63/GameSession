package com.example.gamesession.authentication.presentation.admin

import com.arkivanov.decompose.ComponentContext
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.usecase.UpdateUserUseCase
import com.example.gamesession.utils.AppDependencies
import com.example.gamesession.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DefaultAdminPanelComponent(
    componentContext: ComponentContext,
    private val user: User,
    private val onLogoutCallback: () -> Unit,
    private val updateUserUseCase: UpdateUserUseCase = AppDependencies.updateUserUseCase,
) : AdminPanelComponent, ComponentContext by componentContext {


    private val scope = componentScope()

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = AdminPanelComponent.Model.serializer())
            ?: AdminPanelComponent.Model(currentUser = user)
    )

    init {
        stateKeeper.register(KEY, strategy = AdminPanelComponent.Model.serializer()) { model.value }
    }

    override val model: StateFlow<AdminPanelComponent.Model>
        get() = _model.asStateFlow()

    override fun onUsersTabClicked() {
        _model.value = _model.value.copy(selectedTabIndex = AdminTab.USERS.index)
    }

    override fun onSessionsTabClicked() {
        _model.value = _model.value.copy(selectedTabIndex = AdminTab.SESSIONS.index)
    }

    override fun onLogout() {
        scope.launch {
            val updatedUser = user.copy(status = false)
            updateUserUseCase(updatedUser)
        }
        onLogoutCallback()
    }
    companion object {
        private const val KEY = "AdminPanelComponent"
    }

}


