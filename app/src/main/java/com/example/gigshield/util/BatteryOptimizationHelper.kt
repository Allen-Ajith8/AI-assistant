package com.example.gigshield.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings

object BatteryOptimizationHelper {

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun requestBatteryOptimizationExemption(context: Context) {
        val intent = Intent().apply {
            action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            data = Uri.parse("package:${context.packageName}")
        }
        context.startActivity(intent)
    }

    fun isIqooDevice(): Boolean {
        val manufacturer = Build.MANUFACTURER.lowercase()
        return manufacturer.contains("vivo") || manufacturer.contains("iqoo")
    }

    fun openAutoStartSettings(context: Context): Intent? {
        if (!isIqooDevice()) return null
        val intent = Intent()
        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
        return intent
    }

    fun showBatteryOptimizationGuide(context: Context): String {
        return "To ensure Shift Tracking works correctly:\n" +
               "1. Allow running in background\n" +
               "2. Disable battery optimization for GigShield\n" +
               if (isIqooDevice()) "3. Enable Auto-start in iQOO Secure settings" else ""
    }
}
