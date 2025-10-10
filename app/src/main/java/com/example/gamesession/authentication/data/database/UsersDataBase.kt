package com.example.gamesession.authentication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UsersDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

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