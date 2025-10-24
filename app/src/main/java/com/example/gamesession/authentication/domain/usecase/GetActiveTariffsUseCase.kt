package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.SessionTariff
import com.example.gamesession.authentication.domain.repository.SessionTariffRepository
import kotlinx.coroutines.flow.Flow

class GetActiveTariffsUseCase(
    private val sessionTariffRepository: SessionTariffRepository
) {
    operator fun invoke(): Flow<List<SessionTariff>> {
        return sessionTariffRepository.getActiveTariffs()
    }
}









