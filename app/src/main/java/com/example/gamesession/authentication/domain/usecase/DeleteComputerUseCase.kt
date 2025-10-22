package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.Computer
import com.example.gamesession.authentication.domain.repository.ComputerRepository

class DeleteComputerUseCase(
    private val computerRepository: ComputerRepository
) {
    suspend operator fun invoke(computer: Computer) {
        computerRepository.deleteComputer(computer)
    }
}






