package com.example.gamesession.authentication.presentation.user

import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.model.SessionStatus
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface UserComponent {
    val model: StateFlow<Model>

    fun onLogout()

    @Serializable
    data class Model(
        val currentUser: User,
        val userSessions: List<UserSessionItem> = emptyList()
    )

    @Serializable
    data class UserSessionItem(
        val id: Int,
        val date: String,
        val time: String,
        val duration: Double,
        val computerName: String,
        val price: Int,
        val status: SessionStatus = SessionStatus.SCHEDULED,
        val actualDuration: String = "0м",
        val startTime: Long = 0L,
        val durationHours: Double = 0.0
    )
}




