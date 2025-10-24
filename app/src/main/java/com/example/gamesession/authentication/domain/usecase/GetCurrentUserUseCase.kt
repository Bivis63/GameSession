package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.User
import com.example.gamesession.authentication.domain.repository.UserRepository

class GetCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User? {
        return userRepository.getCurrentUser()
    }
}