package com.example.gamesession.authentication.data.datasource

import com.example.gamesession.authentication.data.database.ComputerDao
import com.example.gamesession.authentication.data.database.ComputerMapper
import com.example.gamesession.authentication.domain.model.Computer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ComputerDataSource(
    private val computerDao: ComputerDao
) {
    fun getAllComputers(): Flow<List<Computer>> {
        return computerDao.getAllComputers().map { entities ->
            ComputerMapper.toDomainList(entities)
        }
    }

    fun getAvailableComputers(): Flow<List<Computer>> {
        return computerDao.getAvailableComputers().map { entities ->
            ComputerMapper.toDomainList(entities)
        }
    }

    suspend fun getComputerById(id: Int): Computer? {
        val entity = computerDao.getComputerById(id)
        return entity?.let { ComputerMapper.toDomain(it) }
    }

    suspend fun insertComputer(computer: Computer) {
        val entity = ComputerMapper.toEntity(computer)
        computerDao.insertComputer(entity)
    }

    suspend fun updateComputer(computer: Computer) {
        val entity = ComputerMapper.toEntity(computer)
        computerDao.updateComputer(entity)
    }

    suspend fun updateComputerAvailability(id: Int, isAvailable: Boolean) {
        computerDao.updateComputerAvailability(id, isAvailable)
    }

    suspend fun deleteComputer(computer: Computer) {
        val entity = ComputerMapper.toEntity(computer)
        computerDao.deleteComputer(entity)
    }

    suspend fun isCodeExists(code: String, excludeComputerId: Int = 0): Boolean {
        return computerDao.isCodeExists(code, excludeComputerId) > 0
    }

    suspend fun initializeDefaults() {
        val existingComputers = computerDao.getAllComputersOnce()
        if (existingComputers.isEmpty()) {
            val defaults = listOf(
                Computer(code = "PC-001", name = "ПК-1", isAvailable = true),
                Computer(code = "PC-002", name = "ПК-2", isAvailable = true),
                Computer(code = "PC-003", name = "ПК-3", isAvailable = true)
            )
            defaults.forEach { insertComputer(it) }
        }
    }

    suspend fun getNextAvailableCode(): String {
        val computers = computerDao.getAllComputersOnce()
        val existingNumbers = computers.mapNotNull { 
            it.code.removePrefix("PC-").toIntOrNull() 
        }
        val nextNumber = if (existingNumbers.isEmpty()) 1 else (existingNumbers.maxOrNull() ?: 0) + 1
        return "PC-${nextNumber.toString().padStart(3, '0')}"
    }
}

