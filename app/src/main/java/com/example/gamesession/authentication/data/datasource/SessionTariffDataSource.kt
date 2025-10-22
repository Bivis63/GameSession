package com.example.gamesession.authentication.data.datasource

import com.example.gamesession.authentication.data.database.SessionTariffDao
import com.example.gamesession.authentication.data.database.SessionTariffMapper
import com.example.gamesession.authentication.domain.model.SessionTariff
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionTariffDataSource(
    private val sessionTariffDao: SessionTariffDao
) {
    fun getActiveTariffs(): Flow<List<SessionTariff>> {
        return sessionTariffDao.getActiveTariffs().map { entities ->
            SessionTariffMapper.toDomainList(entities)
        }
    }

    fun getAllTariffs(): Flow<List<SessionTariff>> {
        return sessionTariffDao.getAllTariffs().map { entities ->
            SessionTariffMapper.toDomainList(entities)
        }
    }

    suspend fun getTariffById(id: Int): SessionTariff? {
        val entity = sessionTariffDao.getTariffById(id)
        return entity?.let { SessionTariffMapper.toDomain(it) }
    }

    suspend fun insertTariff(tariff: SessionTariff) {
        val entity = SessionTariffMapper.toEntity(tariff)
        sessionTariffDao.insertTariff(entity)
    }

    suspend fun updateTariff(tariff: SessionTariff) {
        val entity = SessionTariffMapper.toEntity(tariff)
        sessionTariffDao.updateTariff(entity)
    }

    suspend fun initializeDefaults() {
        val existing = sessionTariffDao.getAllTariffsOnce()
        if (existing.isEmpty()) {
            val defaults = listOf(
                SessionTariff(name = "1 час", durationHours = 1.0, price = 300),
                SessionTariff(name = "30 минут", durationHours = 0.5, price = 150)
            )
            defaults.forEach {
                sessionTariffDao.insertTariff(SessionTariffMapper.toEntity(it))
            }
        }
    }
}






