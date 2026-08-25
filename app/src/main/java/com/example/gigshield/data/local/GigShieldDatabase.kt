package com.example.gigshield.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.gigshield.data.model.DrivingEvent
import com.example.gigshield.data.model.DrivingEventType
import com.example.gigshield.data.model.InsuranceTier
import com.example.gigshield.data.model.Policy
import com.example.gigshield.data.model.SafeRiderScore

class Converters {
    @TypeConverter
    fun fromInsuranceTier(value: InsuranceTier) = value.name

    @TypeConverter
    fun toInsuranceTier(value: String) = enumValueOf<InsuranceTier>(value)

    @TypeConverter
    fun fromDrivingEventType(value: DrivingEventType) = value.name

    @TypeConverter
    fun toDrivingEventType(value: String) = enumValueOf<DrivingEventType>(value)
}

@Database(
    entities = [
        com.example.gigshield.data.model.DrivingEvent::class,
        com.example.gigshield.data.model.SafeRiderScore::class,
        com.example.gigshield.data.model.Policy::class,
        WorkSessionEntity::class,
        ClaimEntity::class,
        TransactionEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GigShieldDatabase : RoomDatabase() {
    
    abstract fun drivingEventDao(): DrivingEventDao
    abstract fun scoreDao(): ScoreDao
    abstract fun policyDao(): PolicyDao
    abstract fun workSessionDao(): WorkSessionDao
    abstract fun claimDao(): ClaimDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: GigShieldDatabase? = null

        fun getInstance(context: Context): GigShieldDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GigShieldDatabase::class.java,
                    "gigshield_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
