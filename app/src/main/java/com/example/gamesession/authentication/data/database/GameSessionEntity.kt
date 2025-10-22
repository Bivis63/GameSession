package com.example.gamesession.authentication.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "game_sessions",
    indices = [
        Index(value = ["computerId"]),
        Index(value = ["userId"])
    ]
)
data class GameSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val time: String,
    val durationHours: Double,
    val computerId: Int,
    val userId: Int,
    val isActive: Boolean = true
)

