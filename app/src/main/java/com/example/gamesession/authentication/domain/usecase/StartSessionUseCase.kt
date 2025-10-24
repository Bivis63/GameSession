package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.repository.GameSessionRepository

class StartSessionUseCase(
    private val gameSessionRepository: GameSessionRepository
) {
    suspend operator fun invoke(sessionId: Int): Result<GameSession> {
        return gameSessionRepository.startSession(sessionId)
    }
}


