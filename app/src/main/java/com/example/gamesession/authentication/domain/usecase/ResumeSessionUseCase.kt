package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.GameSessionRepository

class ResumeSessionUseCase(
    private val gameSessionRepository: GameSessionRepository
) {
    suspend operator fun invoke(sessionId: Int): Result<Unit> {
        return gameSessionRepository.resumeSession(sessionId)
    }
}


