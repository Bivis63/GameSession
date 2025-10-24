package com.example.gamesession.authentication.domain.repository

import com.example.gamesession.authentication.domain.model.SessionTariff
import kotlinx.coroutines.flow.Flow

interface SessionTariffRepository {
    fun getActiveTariffs(): Flow<List<SessionTariff>>
    fun getAllTariffs(): Flow<List<SessionTariff>>
    suspend fun getTariffById(id: Int): SessionTariff?
    suspend fun insertTariff(tariff: SessionTariff)
    suspend fun updateTariff(tariff: SessionTariff)
    suspend fun initializeDefaults()

}









