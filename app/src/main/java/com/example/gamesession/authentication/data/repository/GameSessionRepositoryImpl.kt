package com.example.gamesession.authentication.data.repository

import com.example.gamesession.authentication.data.datasource.GameSessionDataSource
import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.repository.GameSessionRepository
import kotlinx.coroutines.flow.Flow

class GameSessionRepositoryImpl(
    private val gameSessionDataSource: GameSessionDataSource
) : GameSessionRepository {

    override fun getAllGameSessions(): Flow<List<GameSession>> {
        return gameSessionDataSource.getAllGameSessions()
    }

    override suspend fun getGameSessionById(id: Int): GameSession? {
        return gameSessionDataSource.getGameSessionById(id)
    }

    override fun getGameSessionsByComputer(computerId: Int): Flow<List<GameSession>> {
        return gameSessionDataSource.getGameSessionsByComputer(computerId)
    }

    override fun getGameSessionsByUser(userId: Int): Flow<List<GameSession>> {
        return gameSessionDataSource.getGameSessionsByUser(userId)
    }

    override fun getActiveGameSessions(): Flow<List<GameSession>> {
        return gameSessionDataSource.getActiveGameSessions()
    }

    override suspend fun insertGameSession(gameSession: GameSession) {
        gameSessionDataSource.insertGameSession(gameSession)
    }

    override suspend fun updateGameSession(gameSession: GameSession) {
        gameSessionDataSource.updateGameSession(gameSession)
    }

    override suspend fun updateGameSessionStatus(id: Int, isActive: Boolean) {
        gameSessionDataSource.updateGameSessionStatus(id, isActive)
    }

    override suspend fun getAvailableTimeSlots(computerId: Int, date: String, durationHours: Double): List<String> {
       return gameSessionDataSource.getAvailableTimeSlots(computerId, date, durationHours)
    }

    override suspend fun isComputerAvailable(computerId: Int, date: String, time: String, durationHours: Double): Boolean {
        return gameSessionDataSource.isComputerAvailable(computerId, date, time, durationHours)
    }
}






