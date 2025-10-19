package com.example.gamesession.authentication.presentation.user

import com.example.gamesession.authentication.domain.model.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface AdminComponent {
    val model: StateFlow<Model>

    fun onAddUserClicked()
    fun onEditUserClicked(userId: Int)
    fun onDeleteUserClicked(userId: Int)
    fun onToggleUserBlock(userId: Int)
    fun onSearchQueryChanged(query: String)
    fun onAddUserDialogDismissed()
    fun onAddUser(
        login: String,
        password: String,
        nickName: String,
        phoneNumber: String
    )
    fun onEditUser(
        userId: Int,
        login: String,
        password: String,
        nickName: String,
        phoneNumber: String
    )
    fun onClearError()
    fun onLogout()

    @Serializable
    data class Model(
        val users: List<User> = emptyList(),
        val filteredUsers: List<User> = emptyList(),
        val searchQuery: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val showAddUserDialog: Boolean = false,
        val showEditUserDialog: Boolean = false,
        val editingUser: User? = null
    )
}