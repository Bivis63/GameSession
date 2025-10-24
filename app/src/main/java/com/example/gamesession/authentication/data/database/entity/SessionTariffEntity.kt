package com.example.gamesession.authentication.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session_tariffs",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class SessionTariffEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val durationHours: Double,
    val price: Int,
    val isActive: Boolean = true
)






