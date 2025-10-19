package com.example.gamesession.authentication.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.gamesession.authentication.domain.model.RuleSettings

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["nickName"], unique = true),
        Index(value = ["phoneNumber"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val login :String,
    val password: String,
    val nickName: String,
    val phoneNumber: String,
    val rule: RuleSettings = RuleSettings.USER,
    val isBlocked: Boolean = false,
    val status: Boolean = false
)
