package com.example.gamesession.authentication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSessionDao {
    @Query("SELECT * FROM game_sessions")
    fun getAllGameSessions(): Flow<List<GameSessionEntity>>

    @Query("SELECT * FROM game_sessions WHERE id = :id LIMIT 1")
    suspend fun getGameSessionById(id: Int): GameSessionEntity?

    @Query("SELECT * FROM game_sessions WHERE computerId = :computerId")
    fun getGameSessionsByComputer(computerId: Int): Flow<List<GameSessionEntity>>

    @Query("SELECT * FROM game_sessions WHERE computerId = :computerId")
    suspend fun getGameSessionsByComputerOnce(computerId: Int): List<GameSessionEntity>

    @Query("SELECT * FROM game_sessions WHERE userId = :userId")
    fun getGameSessionsByUser(userId: Int): Flow<List<GameSessionEntity>>

    @Query("SELECT * FROM game_sessions WHERE isActive = 1")
    fun getActiveGameSessions(): Flow<List<GameSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameSession(gameSession: GameSessionEntity)

    @Update
    suspend fun updateGameSession(gameSession: GameSessionEntity)

    @Query("UPDATE game_sessions SET isActive = :isActive WHERE id = :id")
    suspend fun updateGameSessionStatus(id: Int, isActive: Boolean)
}






