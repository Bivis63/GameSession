package com.example.gamesession.authentication.data.database

import com.example.gamesession.authentication.domain.model.GameSession

object GameSessionMapper {
    fun toDomain(entity: GameSessionEntity): GameSession {
        return GameSession(
            id = entity.id,
            date = entity.date,
            time = entity.time,
            durationHours = entity.durationHours,
            computerId = entity.computerId,
            userId = entity.userId,
            isActive = entity.isActive
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
            isActive = domain.isActive
        )
    }

    fun toDomainList(entities: List<GameSessionEntity>): List<GameSession> {
        return entities.map { toDomain(it) }
    }
}

