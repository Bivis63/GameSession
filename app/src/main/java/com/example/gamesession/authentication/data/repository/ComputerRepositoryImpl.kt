package com.example.gamesession.authentication.data.repository

import com.example.gamesession.authentication.data.datasource.ComputerDataSource
import com.example.gamesession.authentication.domain.model.Computer
import com.example.gamesession.authentication.domain.repository.ComputerRepository
import kotlinx.coroutines.flow.Flow

class ComputerRepositoryImpl(
    private val computerDataSource: ComputerDataSource
) : ComputerRepository {

    override fun getAllComputers(): Flow<List<Computer>> {
        return computerDataSource.getAllComputers()
    }

    override fun getAvailableComputers(): Flow<List<Computer>> {
        return computerDataSource.getAvailableComputers()
    }

    override suspend fun getComputerById(id: Int): Computer? {
        return computerDataSource.getComputerById(id)
    }

    override suspend fun insertComputer(computer: Computer) {
        computerDataSource.insertComputer(computer)
    }

    override suspend fun updateComputer(computer: Computer) {
        computerDataSource.updateComputer(computer)
    }

    override suspend fun deleteComputer(computer: Computer) {
        computerDataSource.deleteComputer(computer)
    }

    override suspend fun updateComputerAvailability(id: Int, isAvailable: Boolean) {
        computerDataSource.updateComputerAvailability(id, isAvailable)
    }

    override suspend fun isCodeExists(code: String, excludeComputerId: Int): Boolean {
        return computerDataSource.isCodeExists(code, excludeComputerId)
    }

    override suspend fun getNextAvailableCode(): String {
        return computerDataSource.getNextAvailableCode()
    }

    override suspend fun initializeDefaults() {
        computerDataSource.initializeDefaults()
    }
}

