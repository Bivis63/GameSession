package com.example.gamesession.authentication.domain.usecase

import com.example.gamesession.authentication.domain.model.Computer
import com.example.gamesession.authentication.domain.repository.ComputerRepository
import kotlinx.coroutines.flow.Flow

class GetAvailableComputersUseCase(
    private val computerRepository: ComputerRepository
) {
    operator fun invoke(): Flow<List<Computer>> {
        return computerRepository.getAvailableComputers()
    }
}

