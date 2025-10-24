package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.repository.GameSessionRepository

class InsertGameSessionUseCase(
    private val gameSessionRepository: GameSessionRepository
) {
    suspend operator fun invoke(gameSession: GameSession) {
        gameSessionRepository.insertGameSession(gameSession)
    }
}









