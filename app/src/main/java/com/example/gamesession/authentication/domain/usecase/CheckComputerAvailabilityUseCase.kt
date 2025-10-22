package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.GameSessionRepository

class CheckComputerAvailabilityUseCase(
    private val gameSessionRepository: GameSessionRepository
) {
    suspend operator fun invoke(
        computerId: Int,
        date: String,
        time: String,
        durationHours: Double
    ): Boolean {
        return gameSessionRepository.isComputerAvailable(computerId, date, time, durationHours)
    }
}






