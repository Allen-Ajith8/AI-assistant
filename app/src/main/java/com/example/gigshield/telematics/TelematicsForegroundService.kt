package com.example.gigshield.telematics

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.gigshield.data.local.GigShieldDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TelematicsForegroundService : Service() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private var wakeLock: PowerManager.WakeLock? = null
    
    private lateinit var sensorDataCollector: SensorDataCollector
    private lateinit var locationTracker: LocationTracker
    private lateinit var drivingEventDetector: DrivingEventDetector
    
    private var shiftStartTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        val db = GigShieldDatabase.getInstance(this)
        
        drivingEventDetector = DrivingEventDetector { event ->
            serviceScope.launch {
                db.drivingEventDao().insert(event)
            }
        }
        
        sensorDataCollector = SensorDataCollector(this, drivingEventDetector)
        locationTracker = LocationTracker(this) { location ->
            drivingEventDetector.updateSpeed(location.speed * 3.6f) // m/s to km/h
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        
        shiftStartTime = System.currentTimeMillis()
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Shift Tracking")
            .setContentText("Active shift ongoing")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
            
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(1, notification)
        }
        
        acquireWakeLock()
        sensorDataCollector.start()
        locationTracker.start()
        
        serviceScope.launch {
            while(true) {
                delay(60000)
                updateNotificationTimer()
            }
        }
        
        return START_STICKY
    }
    
    private fun updateNotificationTimer() {
        val durationMs = System.currentTimeMillis() - shiftStartTime
        val minutes = (durationMs / 60000).toInt()
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Shift Tracking")
            .setContentText("Active shift ongoing: $minutes mins")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
            
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GigShield::TelematicsWakeLock")
        wakeLock?.acquire(12 * 60 * 60 * 1000L) // Max 12 hours
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        sensorDataCollector.stop()
        locationTracker.stop()
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Shift Tracking",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "telematics_channel"
        private const val ACTION_STOP = "com.example.gigshield.STOP_TELEMATICS"

        fun start(context: Context) {
            val intent = Intent(context, TelematicsForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        
        fun stop(context: Context) {
            val intent = Intent(context, TelematicsForegroundService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
