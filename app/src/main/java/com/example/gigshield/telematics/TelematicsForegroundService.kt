package com.example.gigshield.telematics

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.gigshield.MainActivity
import com.example.gigshield.R
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
    private var shiftStartElapsed: Long = 0 // For chronometer base

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
            drivingEventDetector.updateSpeed(location.speed * 3.6f)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                // Send broadcast to open the app and show summary
                val openIntent = Intent(this, MainActivity::class.java).apply {
                    this.action = ACTION_SHOW_SUMMARY
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                startActivity(openIntent)
                stopSelf()
                return START_NOT_STICKY
            }
        }

        shiftStartTime = System.currentTimeMillis()
        shiftStartElapsed = SystemClock.elapsedRealtime()

        startForegroundWithNotification()
        acquireWakeLock()
        sensorDataCollector.start()
        locationTracker.start()

        // Periodic notification update (every 30 seconds)
        serviceScope.launch {
            while (true) {
                delay(30_000)
                updateLiveNotification()
            }
        }

        return START_STICKY
    }

    private fun startForegroundWithNotification() {
        val notification = buildLiveNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun buildLiveNotification(): android.app.Notification {
        // Tap notification → open app
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_SHOW_SUMMARY
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // "Stop Shift" action → stop service and show summary
        val stopIntent = Intent(this, TelematicsForegroundService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle("⛑ GigGuard • Shift Active")
            .setContentText("Shift Shield • Protected")
            .setSubText("Tap to view summary")
            .setUsesChronometer(true)
            .setWhen(shiftStartTime)
            .setOngoing(true)
            .setShowWhen(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                android.R.drawable.ic_media_pause,
                "Stop Shift",
                stopPendingIntent
            )
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    private fun updateLiveNotification() {
        val durationMs = System.currentTimeMillis() - shiftStartTime
        val minutes = (durationMs / 60_000).toInt()
        val distanceEstimate = (minutes / 60f) * 22f // ~22 km/h

        // Tap notification → open app and show summary
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_SHOW_SUMMARY
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, TelematicsForegroundService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle("⛑ GigGuard • Shift Active")
            .setContentText("Shift Shield • ${String.format("%.1f", distanceEstimate)} km covered")
            .setSubText("Tap to view summary")
            .setUsesChronometer(true)
            .setWhen(shiftStartTime)
            .setOngoing(true)
            .setShowWhen(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                android.R.drawable.ic_media_pause,
                "Stop Shift",
                stopPendingIntent
            )
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
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
                NotificationManager.IMPORTANCE_DEFAULT // Visible, no sound by default
            ).apply {
                description = "Shows your active shift status and live timer"
                setShowBadge(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "telematics_channel"
        private const val NOTIFICATION_ID = 1
        const val ACTION_STOP = "com.example.gigshield.STOP_TELEMATICS"
        const val ACTION_SHOW_SUMMARY = "com.example.gigshield.SHOW_SUMMARY"

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
