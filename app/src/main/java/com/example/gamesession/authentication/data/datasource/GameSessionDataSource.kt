package com.example.gamesession.authentication.data.datasource

import com.example.gamesession.authentication.data.database.dao.GameSessionDao
import com.example.gamesession.authentication.data.database.entity.GameSessionEntity
import com.example.gamesession.authentication.data.database.mapper.GameSessionMapper
import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.SessionStatus
import com.example.gamesession.authentication.domain.model.SessionTariff
import com.example.gamesession.utils.SessionUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameSessionDataSource(
    private val gameSessionDao: GameSessionDao
) {
    fun getAllGameSessions(): Flow<List<GameSession>> {
        return gameSessionDao.getAllGameSessions().map { entities ->
            GameSessionMapper.toDomainList(entities)
        }
    }

    suspend fun getGameSessionById(id: Int): GameSession? {
        val entity = gameSessionDao.getGameSessionById(id)
        return entity?.let { GameSessionMapper.toDomain(it) }
    }

    fun getGameSessionsByComputer(computerId: Int): Flow<List<GameSession>> {
        return gameSessionDao.getGameSessionsByComputer(computerId).map { entities ->
            GameSessionMapper.toDomainList(entities)
        }
    }

    fun getGameSessionsByUser(userId: Int): Flow<List<GameSession>> {
        return gameSessionDao.getGameSessionsByUser(userId).map { entities ->
            GameSessionMapper.toDomainList(entities)
        }
    }

    fun getActiveGameSessions(): Flow<List<GameSession>> {
        return gameSessionDao.getActiveGameSessions().map { entities ->
            GameSessionMapper.toDomainList(entities)
        }
    }

    suspend fun insertGameSession(gameSession: GameSession) {
        val entity = GameSessionMapper.toEntity(gameSession)
        gameSessionDao.insertGameSession(entity)
    }

    suspend fun updateGameSession(gameSession: GameSession) {
        val entity = GameSessionMapper.toEntity(gameSession)
        gameSessionDao.updateGameSession(entity)
    }

    suspend fun updateGameSessionStatus(id: Int, status: String) {
        gameSessionDao.updateGameSessionStatus(id, status)
    }

    suspend fun getActiveGameSessionByComputer(computerId: Int): GameSessionEntity? {
        return gameSessionDao.getActiveGameSessionByComputer(computerId)
    }

    suspend fun getAvailableTimeSlots(
        computerId: Int,
        date: String,
        durationHours: Double
    ): List<String> {
        val existingSessions = gameSessionDao.getGameSessionsByComputerOnce(computerId)
            .map(GameSessionMapper::toDomain)

        val occupiedSlots = existingSessions
            .filter { it.date == date && it.status.name in listOf("RUNNING", "PAUSED") }
            .map { session ->
                val startTime = parseTime(session.time)
                val endTime = startTime + (session.durationHours * 60).toInt()
                startTime to endTime
            }
            .sortedBy { it.first }

        val availableSlots = mutableListOf<String>()
        val durationMinutes = (durationHours * 60).toInt()

        val startHour = 9
        val endHour = 22

        for (hour in startHour until endHour) {
            for (minute in 0..30 step 30) {
                val slotStart = hour * 60 + minute
                val slotEnd = slotStart + durationMinutes

                if (slotEnd <= endHour * 60) {
                    val isAvailable = occupiedSlots.none { (occupiedStart, occupiedEnd) ->
                        !(slotEnd <= occupiedStart || slotStart >= occupiedEnd)
                    }

                    if (isAvailable) {
                        val timeString = formatTime(slotStart)
                        availableSlots.add(timeString)
                    }
                }
            }
        }

        return availableSlots
    }

    suspend fun isComputerAvailable(
        computerId: Int,
        date: String,
        time: String,
        durationHours: Double
    ): Boolean {
        val entities = gameSessionDao.getGameSessionsByComputerOnce(computerId)
        val existingSessions = entities.map { GameSessionMapper.toDomain(it) }

        val requestedStartTime = parseTime(time)
        val requestedEndTime = requestedStartTime + (durationHours * 60).toInt()

        return existingSessions
            .filter { it.date == date && it.status.name in listOf("RUNNING", "PAUSED") }
            .none { existingSession ->
                val existingStartTime = parseTime(existingSession.time)
                val existingEndTime =
                    existingStartTime + (existingSession.durationHours * 60).toInt()

                !(requestedEndTime <= existingStartTime || requestedStartTime >= existingEndTime)
            }
    }

    private fun parseTime(timeString: String): Int {
        val parts = timeString.split(":")
        return parts[0].toInt() * 60 + parts[1].toInt()
    }

    private fun formatTime(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return String.format("%02d:%02d", hours, mins)
    }


    suspend fun startSession(sessionId: Int): Result<GameSession> {
        return try {
            val session = getGameSessionById(sessionId)
                ?: return Result.failure(Exception("Сессия не найдена"))

            if (session.status != SessionStatus.SCHEDULED) {
                return Result.failure(Exception("Сессия уже запущена или завершена"))
            }


            val activeSession = getActiveGameSessionByComputer(session.computerId)
            if (activeSession != null) {
                return Result.failure(Exception("На этом компьютере уже запущена сессия"))
            }

            val updatedSession = session.copy(
                status = SessionStatus.RUNNING,
                startTime = System.currentTimeMillis()
            )

            updateGameSession(updatedSession)
            Result.success(updatedSession)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun pauseSession(sessionId: Int): Result<Unit> {
        return try {
            val session = getGameSessionById(sessionId)
                ?: return Result.failure(Exception("Сессия не найдена"))

            if (session.status != SessionStatus.RUNNING) {
                return Result.failure(Exception("Сессия не запущена"))
            }

            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - session.startTime
            val newPausedTime = session.pausedTime + elapsedTime

            val updatedSession = session.copy(
                status = SessionStatus.PAUSED,
                pausedTime = newPausedTime
            )

            updateGameSession(updatedSession)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resumeSession(sessionId: Int): Result<Unit> {
        return try {
            val session = getGameSessionById(sessionId)
                ?: return Result.failure(Exception("Сессия не найдена"))

            if (session.status != SessionStatus.PAUSED) {
                return Result.failure(Exception("Сессия не на паузе"))
            }

            val updatedSession = session.copy(
                status = SessionStatus.RUNNING,
                startTime = System.currentTimeMillis()
            )

            updateGameSession(updatedSession)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun finishSession(sessionId: Int, getTariffById: suspend (Int) -> SessionTariff?): Result<Int> {
        return try {
            val session = getGameSessionById(sessionId)
                ?: return Result.failure(Exception("Сессия не найдена"))

            if (session.status == SessionStatus.FINISHED) {
                return Result.failure(Exception("Сессия уже завершена"))
            }

            val actualMinutes = SessionUtils.calculateActualDurationMinutes(session)
            val billedMinutes = SessionUtils.calculateBilledMinutes(actualMinutes)
            
            val tariff = getTariffById(session.tariffId)
                ?: return Result.failure(Exception("Тариф не найден"))

            val totalCost = SessionUtils.calculateSessionCost(session, tariff)

            val updatedSession = session.copy(
                status = SessionStatus.FINISHED,
                actualDurationMinutes = actualMinutes,
                billedMinutes = billedMinutes,
                totalCost = totalCost
            )

            updateGameSession(updatedSession)
            Result.success(totalCost)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

