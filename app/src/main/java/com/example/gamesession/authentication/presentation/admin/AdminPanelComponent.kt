package com.example.gamesession.authentication.presentation.admin

import com.example.gamesession.authentication.domain.model.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

enum class AdminTab(val index: Int) {
    USERS(0),
    SESSIONS(1);
    
    companion object {
        fun fromIndex(index: Int): AdminTab = entries.find { it.index == index } ?: USERS
    }
}

interface AdminPanelComponent {
    val model: StateFlow<Model>

    fun onUsersTabClicked()
    fun onSessionsTabClicked()
    fun onLogout()

    @Serializable
    data class Model(
        val selectedTabIndex: Int = 0,
        val currentUser: User
    )
}





