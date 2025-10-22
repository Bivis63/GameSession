package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.ComputerRepository

class GetNextComputerCodeUseCase(
    private val computerRepository: ComputerRepository
) {
    suspend operator fun invoke(): String {
        return computerRepository.getNextAvailableCode()
    }
}






