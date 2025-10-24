package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.UserRepository

class InitializeUsersDataUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke() {
        userRepository.initializeSampleData()
    }
}