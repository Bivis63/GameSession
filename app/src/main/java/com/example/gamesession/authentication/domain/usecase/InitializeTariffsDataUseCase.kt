package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.SessionTariffRepository

class InitializeTariffsDataUseCase(
    private val sessionTariffRepository: SessionTariffRepository
) {
    suspend operator fun invoke() {
        sessionTariffRepository.initializeDefaults()
    }
}






