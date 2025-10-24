package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.UserRepository

class SetCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int) {
        userRepository.setUserAsCurrent(userId)
    }
}