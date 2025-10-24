package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.GameSessionRepository

class FinishSessionUseCase(
    private val gameSessionRepository: GameSessionRepository
) {
    suspend operator fun invoke(sessionId: Int): Result<Int> {
        return gameSessionRepository.finishSession(sessionId)
    }
}


