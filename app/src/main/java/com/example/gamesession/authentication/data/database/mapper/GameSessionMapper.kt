package com.example.gamesession.authentication.data.database.mapper

import com.example.gamesession.authentication.data.database.entity.GameSessionEntity
import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.model.SessionStatus

object GameSessionMapper {
    fun toDomain(entity: GameSessionEntity): GameSession {
        return GameSession(
            id = entity.id,
            date = entity.date,
            time = entity.time,
            durationHours = entity.durationHours,
            computerId = entity.computerId,
            userId = entity.userId,
            tariffId = entity.tariffId,
            status = SessionStatus.valueOf(entity.status),
            startTime = entity.startTime,
            pausedTime = entity.pausedTime,
            actualDurationMinutes = entity.actualDurationMinutes,
            billedMinutes = entity.billedMinutes,
            totalCost = entity.totalCost
        )
    }

    fun toEntity(domain: GameSession): GameSessionEntity {
        return GameSessionEntity(
            id = domain.id,
            date = domain.date,
            time = domain.time,
            durationHours = domain.durationHours,
            computerId = domain.computerId,
            userId = domain.userId,
            tariffId = domain.tariffId,
            status = domain.status.name,
            startTime = domain.startTime,
            pausedTime = domain.pausedTime,
            actualDurationMinutes = domain.actualDurationMinutes,
            billedMinutes = domain.billedMinutes,
            totalCost = domain.totalCost
        )
    }

    fun toDomainList(entities: List<GameSessionEntity>): List<GameSession> {
        return entities.map { toDomain(it) }
    }
}

