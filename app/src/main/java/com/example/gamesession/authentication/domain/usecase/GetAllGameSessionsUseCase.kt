package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.GameSession
import com.example.gamesession.authentication.domain.repository.GameSessionRepository
import kotlinx.coroutines.flow.Flow

class GetAllGameSessionsUseCase(
    private val gameSessionRepository: GameSessionRepository
) {
    operator fun invoke(): Flow<List<GameSession>> {
        return gameSessionRepository.getAllGameSessions()
    }
}









