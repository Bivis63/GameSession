package com.example.gamesession.authentication.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ComputerDao {
    @Query("SELECT * FROM computers")
    fun getAllComputers(): Flow<List<ComputerEntity>>

    @Query("SELECT * FROM computers WHERE isAvailable = 1")
    fun getAvailableComputers(): Flow<List<ComputerEntity>>

    @Query("SELECT * FROM computers")
    suspend fun getAllComputersOnce(): List<ComputerEntity>

    @Query("SELECT * FROM computers WHERE id = :id LIMIT 1")
    suspend fun getComputerById(id: Int): ComputerEntity?

    @Query("SELECT COUNT(*) FROM computers WHERE code = :code AND id != :excludeComputerId")
    suspend fun isCodeExists(code: String, excludeComputerId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComputer(computer: ComputerEntity)

    @Update
    suspend fun updateComputer(computer: ComputerEntity)

    @Delete
    suspend fun deleteComputer(computer: ComputerEntity)

    @Query("UPDATE computers SET isAvailable = :isAvailable WHERE id = :id")
    suspend fun updateComputerAvailability(id: Int, isAvailable: Boolean)
}

