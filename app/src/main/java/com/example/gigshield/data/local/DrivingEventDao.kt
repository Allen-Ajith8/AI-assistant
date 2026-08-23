package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gigshield.data.model.DrivingEvent
import com.example.gigshield.data.model.DrivingEventType

@Dao
interface DrivingEventDao {
    @Insert
    suspend fun insert(event: DrivingEvent)

    @Query("SELECT * FROM driving_events WHERE timestamp BETWEEN :start AND :end")
    suspend fun getEventsForShift(start: Long, end: Long): List<DrivingEvent>

    @Query("SELECT COUNT(*) FROM driving_events WHERE type = :type AND timestamp BETWEEN :start AND :end")
    suspend fun getEventCountByType(type: DrivingEventType, start: Long, end: Long): Int

    @Query("DELETE FROM driving_events WHERE timestamp < :timestamp")
    suspend fun deleteOldEvents(timestamp: Long)
}
