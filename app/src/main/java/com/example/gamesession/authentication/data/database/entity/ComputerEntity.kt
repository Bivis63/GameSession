package com.example.gamesession.authentication.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "computers",
    indices = [
        Index(value = ["code"], unique = true)
    ]
)
data class ComputerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val name: String,
    val isAvailable: Boolean = true
)

