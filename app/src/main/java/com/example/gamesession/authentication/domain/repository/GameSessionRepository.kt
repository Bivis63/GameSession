package com.example.gamesession.authentication.domain.repository

import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.SessionTariff
import kotlinx.coroutines.flow.Flow

interface GameSessionRepository {
    fun getAllGameSessions(): Flow<List<GameSession>>
    suspend fun getGameSessionById(id: Int): GameSession?
    fun getGameSessionsByComputer(computerId: Int): Flow<List<GameSession>>
    fun getGameSessionsByUser(userId: Int): Flow<List<GameSession>>
    fun getActiveGameSessions(): Flow<List<GameSession>>
    suspend fun insertGameSession(gameSession: GameSession)
    suspend fun updateGameSession(gameSession: GameSession)
    suspend fun updateGameSessionStatus(id: Int, status: String)
    suspend fun getAvailableTimeSlots(computerId: Int, date: String, durationHours: Double): List<String>
    suspend fun isComputerAvailable(computerId: Int, date: String, time: String, durationHours: Double): Boolean
    suspend fun getActiveGameSessionByComputer(computerId: Int): GameSession?
    suspend fun getTariffById(tariffId: Int): SessionTariff?
    suspend fun startSession(sessionId: Int): Result<GameSession>
    suspend fun pauseSession(sessionId: Int): Result<Unit>
    suspend fun resumeSession(sessionId: Int): Result<Unit>
    suspend fun finishSession(sessionId: Int): Result<Int>
}






