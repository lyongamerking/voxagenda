package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AgendaEvent::class], version = 2, exportSchema = false)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun agendaDao(): AgendaDao

    companion object {
        @Volatile
        private var INSTANCE: AgendaDatabase? = null

        fun getInstance(context: Context): AgendaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaDatabase::class.java,
                    "voxagenda_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
