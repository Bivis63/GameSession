package com.example.gamesession.authentication.data.repository

import com.example.gamesession.authentication.data.datasource.SessionTariffDataSource
import com.example.gamesession.authentication.domain.model.SessionTariff
import com.example.gamesession.authentication.domain.repository.SessionTariffRepository
import kotlinx.coroutines.flow.Flow

class SessionTariffRepositoryImpl(
    private val sessionTariffDataSource: SessionTariffDataSource
) : SessionTariffRepository {

    override fun getActiveTariffs(): Flow<List<SessionTariff>> {
        return sessionTariffDataSource.getActiveTariffs()
    }

    override fun getAllTariffs(): Flow<List<SessionTariff>> {
        return sessionTariffDataSource.getAllTariffs()
    }

    override suspend fun getTariffById(id: Int): SessionTariff? {
        return sessionTariffDataSource.getTariffById(id)
    }

    override suspend fun insertTariff(tariff: SessionTariff) {
        sessionTariffDataSource.insertTariff(tariff)
    }

    override suspend fun updateTariff(tariff: SessionTariff) {
        sessionTariffDataSource.updateTariff(tariff)
    }
    override suspend fun initializeDefaults() {
        sessionTariffDataSource.initializeDefaults()
    }

}






