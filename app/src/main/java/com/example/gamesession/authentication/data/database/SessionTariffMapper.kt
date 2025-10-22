package com.example.gamesession.authentication.data.database

import com.example.gamesession.authentication.domain.model.SessionTariff

object SessionTariffMapper {
    fun toDomain(entity: SessionTariffEntity): SessionTariff {
        return SessionTariff(
            id = entity.id,
            name = entity.name,
            durationHours = entity.durationHours,
            price = entity.price,
            isActive = entity.isActive
        )
    }

    fun toEntity(domain: SessionTariff): SessionTariffEntity {
        return SessionTariffEntity(
            id = domain.id,
            name = domain.name,
            durationHours = domain.durationHours,
            price = domain.price,
            isActive = domain.isActive
        )
    }

    fun toDomainList(entities: List<SessionTariffEntity>): List<SessionTariff> {
        return entities.map { toDomain(it) }
    }
}






