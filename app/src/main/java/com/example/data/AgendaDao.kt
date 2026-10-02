package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDao {
    @Query("SELECT * FROM agenda_events ORDER BY dateTimeEpochMs ASC")
    fun getAllEventsFlow(): Flow<List<AgendaEvent>>

    @Query("SELECT * FROM agenda_events WHERE isMemoryCapsule = 1 ORDER BY dateTimeEpochMs DESC")
    fun getMemoriesFlow(): Flow<List<AgendaEvent>>

    @Query("SELECT * FROM agenda_events WHERE id = :id LIMIT 1")
    suspend fun getEventById(id: Long): AgendaEvent?

    @Query("SELECT COUNT(*) FROM agenda_events")
    suspend fun countEvents(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: AgendaEvent): Long

    @Update
    suspend fun updateEvent(event: AgendaEvent)

    @Delete
    suspend fun deleteEvent(event: AgendaEvent)

    @Query("DELETE FROM agenda_events WHERE id = :id")
    suspend fun deleteEventById(id: Long)
}
