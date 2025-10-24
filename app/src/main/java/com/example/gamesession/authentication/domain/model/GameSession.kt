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
    val tariffId: Int,
    val status: SessionStatus = SessionStatus.SCHEDULED,
    val startTime: Long = 0L,
    val pausedTime: Long = 0L,
    val actualDurationMinutes: Int = 0,
    val billedMinutes: Int = 0,
    val totalCost: Int = 0
)

