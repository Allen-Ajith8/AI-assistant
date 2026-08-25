package com.example.gigshield.ui.work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.*

@Composable
fun DeviceStatusScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
            }
            Text(
                text = "Device Status",
                color = TextHighContrast,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "iQOO DEVICE CONNECTED",
                    color = ElectricBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "READY FOR WORK DETECTION",
                    color = SafetyGreen,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "SENSORS & PERMISSIONS",
            color = TextMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DeviceItem(Icons.Default.LocationOn, "GPS & Location", "Active")
                Divider(color = androidx.compose.ui.graphics.Color(0xFF0A0A0A), modifier = Modifier.padding(vertical = 12.dp))
                DeviceItem(Icons.Default.Vibration, "Motion Sensors", "Active")
                Divider(color = androidx.compose.ui.graphics.Color(0xFF0A0A0A), modifier = Modifier.padding(vertical = 12.dp))
                DeviceItem(Icons.Default.DirectionsRun, "Activity Recognition", "Active")
                Divider(color = androidx.compose.ui.graphics.Color(0xFF0A0A0A), modifier = Modifier.padding(vertical = 12.dp))
                DeviceItem(Icons.Default.BatteryFull, "Battery Optimization", "Ignored (Optimal)")
                Divider(color = androidx.compose.ui.graphics.Color(0xFF0A0A0A), modifier = Modifier.padding(vertical = 12.dp))
                DeviceItem(Icons.Default.Wifi, "Network Connection", "Connected")
            }
        }
    }
}

@Composable
fun DeviceItem(icon: ImageVector, title: String, status: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, color = TextHighContrast, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(text = status, color = SafetyGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
