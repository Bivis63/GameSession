package com.example.gamesession.authentication.presentation.user

import com.arkivanov.decompose.ComponentContext
import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.usecase.DeleteUserUseCase
import com.example.gamesession.authentication.domain.usecase.GetAllUsersUseCase
import com.example.gamesession.authentication.domain.usecase.GetCurrentUserUseCase
import com.example.gamesession.authentication.domain.usecase.InsertUserUseCase
import com.example.gamesession.authentication.domain.usecase.UpdateUserUseCase
import com.example.gamesession.utils.AppDependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DefaultAdminComponent(
    componentContext: ComponentContext,
    private val getAllUsersUseCase: GetAllUsersUseCase = AppDependencies.getAllUsersUseCase,
    private val insertUserUseCase: InsertUserUseCase = AppDependencies.insertUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase = AppDependencies.updateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase = AppDependencies.deleteUserUseCase,
    private val onLogoutCallback: () -> Unit = {},
    private val getCurrentUserUseCase: GetCurrentUserUseCase = AppDependencies.getCurrentUserUseCase,
) : AdminComponent, ComponentContext by componentContext {

    companion object {
        private const val KEY = "AdminComponent"
    }

    private val componentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = AdminComponent.Model.serializer())
            ?: AdminComponent.Model()
    )

    init {
        stateKeeper.register(KEY, strategy = AdminComponent.Model.serializer()) { model.value }
        loadUsers()
    }

    override val model: StateFlow<AdminComponent.Model>
        get() = _model.asStateFlow()

    private fun loadUsers() {
        _model.value = _model.value.copy(isLoading = true, errorMessage = null)

        getAllUsersUseCase()
            .onEach { users ->
                _model.value = _model.value.copy(
                    users = users,
                    filteredUsers = filterUsers(users, _model.value.searchQuery),
                    isLoading = false
                )
            }
            .launchIn(componentScope)
    }

    private fun filterUsers(users: List<User>, query: String): List<User> {
        if (query.isBlank()) return users

        return users.filter { user ->
            user.nickName.contains(query, ignoreCase = true) ||
                    user.phoneNumber.contains(query, ignoreCase = true)
        }
    }

    override fun onAddUserClicked() {
        _model.value = _model.value.copy(showAddUserDialog = true)
    }

    override fun onEditUserClicked(userId: Int) {
        val user = _model.value.users.find { it.id == userId }
        _model.value = _model.value.copy(
            showEditUserDialog = true,
            editingUser = user
        )
    }

    override fun onDeleteUserClicked(userId: Int) {
        val user = _model.value.users.find { it.id == userId }
        user?.let {
            componentScope.launch {
                try {
                    deleteUserUseCase(it)
                    loadUsers()
                } catch (e: Exception) {
                    _model.value = _model.value.copy(
                        errorMessage = "Ошибка при удалении пользователя: ${e.message}"
                    )
                }
            }
        }
    }

    override fun onToggleUserBlock(userId: Int) {
        val user = _model.value.users.find { it.id == userId }
        user?.let {
            val updatedUser = it.copy(isBlocked = !it.isBlocked)
            componentScope.launch {
                try {
                    updateUserUseCase(updatedUser)
                    loadUsers()
                } catch (e: Exception) {
                    _model.value = _model.value.copy(
                        errorMessage = "Ошибка при изменении статуса пользователя: ${e.message}"
                    )
                }
            }
        }
    }

    override fun onSearchQueryChanged(query: String) {
        _model.value = _model.value.copy(
            searchQuery = query,
            filteredUsers = filterUsers(_model.value.users, query)
        )
    }

    override fun onAddUserDialogDismissed() {
        _model.value = _model.value.copy(
            showAddUserDialog = false,
            showEditUserDialog = false,
            editingUser = null
        )
    }

    override fun onAddUser(
        login: String,
        password: String,
        nickName: String,
        phoneNumber: String,
    ) {
        if (login.isBlank() || password.isBlank() || nickName.isBlank() || phoneNumber.isBlank()) {
            _model.value = _model.value.copy(
                errorMessage = "Все поля должны быть заполнены"
            )
            return
        }

        val newUser = User(
            login = login,
            password = password,
            nickName = nickName,
            phoneNumber = phoneNumber
        )

        componentScope.launch {
            try {
                val nickNameExists = AppDependencies.getUserRepository().isNickNameExists(nickName)
                val phoneNumberExists =
                    AppDependencies.getUserRepository().isPhoneNumberExists(phoneNumber)

                if (nickNameExists) {
                    _model.value = _model.value.copy(
                        errorMessage = "Пользователь с таким ником уже существует"
                    )
                    return@launch
                }

                if (phoneNumberExists) {
                    _model.value = _model.value.copy(
                        errorMessage = "Пользователь с таким номером телефона уже существует"
                    )
                    return@launch
                }

                insertUserUseCase(newUser)
                loadUsers()
                onAddUserDialogDismissed()
            } catch (e: Exception) {
                _model.value = _model.value.copy(
                    errorMessage = "Ошибка при добавлении пользователя: ${e.message}"
                )
            }
        }
    }

    override fun onEditUser(
        userId: Int,
        login: String,
        password: String,
        nickName: String,
        phoneNumber: String,
    ) {
        if (login.isBlank() || password.isBlank() || nickName.isBlank() || phoneNumber.isBlank()) {
            _model.value = _model.value.copy(
                errorMessage = "Все поля должны быть заполнены"
            )
            return
        }

        val updatedUser = User(
            id = userId,
            login = login,
            password = password,
            nickName = nickName,
            phoneNumber = phoneNumber
        )

        componentScope.launch {
            try {
                updateUserUseCase(updatedUser)
                loadUsers()
                onAddUserDialogDismissed()
            } catch (e: Exception) {
                _model.value = _model.value.copy(
                    errorMessage = "Ошибка при обновлении пользователя: ${e.message}"
                )
            }
        }
    }

    override fun onClearError() {
        _model.value = _model.value.copy(errorMessage = null)
    }

    override fun onLogout() {
        componentScope.launch {
            val currentUser = getCurrentUserUseCase()
            currentUser?.let { user ->
                val updatedUser = user.copy(status = false)
                updateUserUseCase(updatedUser)
            }
        }
        onLogoutCallback()
    }
}