package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.repository.ComputerRepository

class IsComputerCodeExistsUseCase(
    private val computerRepository: ComputerRepository
) {
    suspend operator fun invoke(code: String, excludeComputerId: Int = 0): Boolean {
        return computerRepository.isCodeExists(code, excludeComputerId)
    }
}






