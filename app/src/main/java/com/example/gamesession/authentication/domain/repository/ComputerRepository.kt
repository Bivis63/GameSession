package com.example.gamesession.authentication.domain.repository

import com.example.gamesession.authentication.domain.model.Computer
import kotlinx.coroutines.flow.Flow

interface ComputerRepository {
    fun getAllComputers(): Flow<List<Computer>>
    fun getAvailableComputers(): Flow<List<Computer>>
    suspend fun getComputerById(id: Int): Computer?
    suspend fun insertComputer(computer: Computer)
    suspend fun updateComputer(computer: Computer)
    suspend fun deleteComputer(computer: Computer)
    suspend fun updateComputerAvailability(id: Int, isAvailable: Boolean)
    suspend fun isCodeExists(code: String, excludeComputerId: Int = 0): Boolean
    suspend fun getNextAvailableCode(): String
    suspend fun initializeDefaults()
}

