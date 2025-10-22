package com.example.gamesession.authentication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionTariffDao {
    @Query("SELECT * FROM session_tariffs WHERE isActive = 1")
    fun getActiveTariffs(): Flow<List<SessionTariffEntity>>

    @Query("SELECT * FROM session_tariffs")
    fun getAllTariffs(): Flow<List<SessionTariffEntity>>

    @Query("SELECT * FROM session_tariffs")
    suspend fun getAllTariffsOnce(): List<SessionTariffEntity>

    @Query("SELECT * FROM session_tariffs WHERE id = :id LIMIT 1")
    suspend fun getTariffById(id: Int): SessionTariffEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTariff(tariff: SessionTariffEntity)

    @Update
    suspend fun updateTariff(tariff: SessionTariffEntity)
}






