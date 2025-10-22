package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.GameSessionRepository

class GetAvailableTimeSlotsUseCase(
    private val gameSessionRepository: GameSessionRepository
) {
    suspend operator fun invoke(
        computerId: Int,
        date: String,
        durationHours: Double
    ): List<String> {
        return gameSessionRepository.getAvailableTimeSlots(computerId, date, durationHours)
    }
}






