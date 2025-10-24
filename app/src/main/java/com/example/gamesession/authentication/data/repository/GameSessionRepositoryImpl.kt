package com.example.gamesession.authentication.data.repository

import com.example.gamesession.authentication.data.database.mapper.GameSessionMapper
import com.example.gamesession.authentication.data.datasource.GameSessionDataSource
import com.example.gamesession.authentication.data.datasource.SessionTariffDataSource
import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.SessionTariff
import com.example.gamesession.authentication.domain.repository.GameSessionRepository
import kotlinx.coroutines.flow.Flow

class GameSessionRepositoryImpl(
    private val gameSessionDataSource: GameSessionDataSource,
    private val sessionTariffDataSource: SessionTariffDataSource
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

    override suspend fun updateGameSessionStatus(id: Int, status: String) {
        gameSessionDataSource.updateGameSessionStatus(id, status)
    }

    override suspend fun getActiveGameSessionByComputer(computerId: Int): GameSession? {
        val entity = gameSessionDataSource.getActiveGameSessionByComputer(computerId)
        return entity?.let { GameSessionMapper.toDomain(it) }
    }

    override suspend fun getAvailableTimeSlots(computerId: Int, date: String, durationHours: Double): List<String> {
       return gameSessionDataSource.getAvailableTimeSlots(computerId, date, durationHours)
    }

    override suspend fun isComputerAvailable(computerId: Int, date: String, time: String, durationHours: Double): Boolean {
        return gameSessionDataSource.isComputerAvailable(computerId, date, time, durationHours)
    }

    override suspend fun getTariffById(tariffId: Int): SessionTariff? {
        return sessionTariffDataSource.getTariffById(tariffId)
    }

    override suspend fun startSession(sessionId: Int): Result<GameSession> {
        return gameSessionDataSource.startSession(sessionId)
    }

    override suspend fun pauseSession(sessionId: Int): Result<Unit> {
        return gameSessionDataSource.pauseSession(sessionId)
    }

    override suspend fun resumeSession(sessionId: Int): Result<Unit> {
        return gameSessionDataSource.resumeSession(sessionId)
    }

    override suspend fun finishSession(sessionId: Int): Result<Int> {
        return gameSessionDataSource.finishSession(sessionId) { tariffId ->
            sessionTariffDataSource.getTariffById(tariffId)
        }
    }
}






