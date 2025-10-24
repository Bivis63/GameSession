package com.example.gamesession.authentication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Computer(
    val id: Int = 0,
    val code: String,
    val name: String,
    val isAvailable: Boolean = true
)

