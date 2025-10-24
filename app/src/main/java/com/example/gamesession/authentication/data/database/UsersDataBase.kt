package com.example.gamesession.authentication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamesession.authentication.data.database.dao.ComputerDao
import com.example.gamesession.authentication.data.database.dao.GameSessionDao
import com.example.gamesession.authentication.data.database.dao.SessionTariffDao
import com.example.gamesession.authentication.data.database.dao.UserDao
import com.example.gamesession.authentication.data.database.entity.ComputerEntity
import com.example.gamesession.authentication.data.database.entity.GameSessionEntity
import com.example.gamesession.authentication.data.database.entity.SessionTariffEntity
import com.example.gamesession.authentication.data.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, ComputerEntity::class, GameSessionEntity::class, SessionTariffEntity::class],
    version = 3,
    exportSchema = false
)
abstract class UsersDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun computerDao(): ComputerDao
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun sessionTariffDao(): SessionTariffDao

    companion object {
        @Volatile
        private var INSTANCE: UsersDataBase? = null

        fun getDataBase(context: Context): UsersDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsersDataBase::class.java,
                    "users_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}