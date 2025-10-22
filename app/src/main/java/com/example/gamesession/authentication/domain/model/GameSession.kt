package com.example.gamesession.authentication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSession(
    val id: Int = 0,
    val date: String,
    val time: String,
    val durationHours: Double,
    val computerId: Int,
    val userId: Int,
    val isActive: Boolean = true
)

