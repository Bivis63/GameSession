package com.example.gamesession.authentication.data.database

import com.example.gamesession.authentication.domain.model.Computer

object ComputerMapper {
    fun toDomain(entity: ComputerEntity): Computer {
        return Computer(
            id = entity.id,
            code = entity.code,
            name = entity.name,
            isAvailable = entity.isAvailable
        )
    }

    fun toEntity(domain: Computer): ComputerEntity {
        return ComputerEntity(
            id = domain.id,
            code = domain.code,
            name = domain.name,
            isAvailable = domain.isAvailable
        )
    }

    fun toDomainList(entities: List<ComputerEntity>): List<Computer> {
        return entities.map { toDomain(it) }
    }
}

