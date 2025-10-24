package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.ComputerRepository

class InitializeComputersDataUseCase(
    private val computerRepository: ComputerRepository
) {
    suspend operator fun invoke() {
        computerRepository.initializeDefaults()
    }
}









