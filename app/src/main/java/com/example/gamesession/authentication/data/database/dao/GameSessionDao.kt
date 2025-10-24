package com.example.gamesession.authentication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gamesession.authentication.data.database.entity.GameSessionEntity
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

    @Query("SELECT * FROM game_sessions WHERE status IN ('SCHEDULED', 'RUNNING', 'PAUSED')")
    fun getActiveGameSessions(): Flow<List<GameSessionEntity>>

    @Query("SELECT * FROM game_sessions WHERE computerId = :computerId AND status IN ('RUNNING', 'PAUSED')")
    suspend fun getActiveGameSessionByComputer(computerId: Int): GameSessionEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertGameSession(gameSession: GameSessionEntity)

    @Update
    suspend fun updateGameSession(gameSession: GameSessionEntity)

    @Query("UPDATE game_sessions SET status = :status WHERE id = :id")
    suspend fun updateGameSessionStatus(id: Int, status: String)

    @Query("UPDATE game_sessions SET status = :status, actualDurationMinutes = :actualDuration, billedMinutes = :billedMinutes, totalCost = :totalCost WHERE id = :id")
    suspend fun finishGameSession(id: Int, status: String, actualDuration: Int, billedMinutes: Int, totalCost: Int)
}