package com.example.gamesession.authentication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val login :String,
    val password: String,
    val nickName: String,
    val phoneNumber: String,
    val rule: RuleSettings = RuleSettings.USER,
    val isBlocked: Boolean = false,
    val status: Boolean = false
)
