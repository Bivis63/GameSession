package com.example.gamesession.authentication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionTariff(
    val id: Int = 0,
    val name: String,
    val durationHours: Double,
    val price: Int,
    val isActive: Boolean = true
)









