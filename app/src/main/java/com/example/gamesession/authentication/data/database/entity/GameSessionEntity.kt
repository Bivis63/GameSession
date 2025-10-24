package com.example.gamesession.authentication.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "game_sessions",
    indices = [
        Index(value = ["computerId"]),
        Index(value = ["userId"]),
        Index(value = ["tariffId"])
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
    val tariffId: Int,
    val status: String = "SCHEDULED",
    val startTime: Long = 0L,
    val pausedTime: Long = 0L,
    val actualDurationMinutes: Int = 0,
    val billedMinutes: Int = 0,
    val totalCost: Int = 0
)

