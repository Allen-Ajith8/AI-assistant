package com.example.gigshield.telematics

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorDataCollector(
    context: Context,
    private val detector: DrivingEventDetector
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val linearAccelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private val gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    
    private val bufferSize = 50
    private val accelBuffer = ArrayDeque<FloatArray>(bufferSize)
    private val gyroBuffer = ArrayDeque<FloatArray>(bufferSize)

    fun start() {
        linearAccelSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        gyroSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        val values = event.values.clone()
        val timestamp = System.currentTimeMillis()

        when (event.sensor.type) {
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                if (accelBuffer.size >= bufferSize) accelBuffer.removeFirst()
                accelBuffer.addLast(values)
                detector.processAcceleration(values[0], values[1], values[2], timestamp)
            }
            Sensor.TYPE_GYROSCOPE -> {
                if (gyroBuffer.size >= bufferSize) gyroBuffer.removeFirst()
                gyroBuffer.addLast(values)
                detector.processGyroscope(values[0], values[1], values[2], timestamp)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
