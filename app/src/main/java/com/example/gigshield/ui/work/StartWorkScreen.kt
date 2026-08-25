package com.example.gigshield.ui.work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.gigshield.theme.*
import com.example.gigshield.ui.SharedViewModel

@Composable
fun StartWorkScreen(viewModel: SharedViewModel, onBack: () -> Unit) {
    var workState by remember { mutableStateOf("READY") }
    val isWorking by viewModel.isWorking.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
        }
        
        Text(
            text = "READY TO START?",
            color = TextHighContrast,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                StatusRow(Icons.Default.CheckCircle, "Connected Gig Platform", "Zomato, Uber", SafetyGreen)
                Spacer(modifier = Modifier.height(12.dp))
                StatusRow(Icons.Default.LocationOn, "Current Location Status", "Active", SafetyGreen)
                Spacer(modifier = Modifier.height(12.dp))
                StatusRow(Icons.Default.Smartphone, "Device Status", "iQOO Connected", SafetyGreen)
                Spacer(modifier = Modifier.height(12.dp))
                StatusRow(Icons.Default.DirectionsRun, "Activity Detection Status", "Ready", SafetyGreen)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        if (workState != "READY" || isWorking) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isWorking) "INSURANCE ACTIVATED\n₹25/day Coverage ₹2,00,000" else when (workState) {
                            "STARTING" -> "STARTING WORK SESSION..."
                            "VERIFYING" -> "VERIFYING ACTIVITY..."
                            "VERIFIED" -> "WORK VERIFIED"
                            "ACTIVATED" -> "INSURANCE ACTIVATED\n₹25/day Coverage ₹2,00,000"
                            else -> ""
                        },
                        color = ElectricBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                scope.launch {
                    workState = "STARTING"
                    delay(1000)
                    workState = "VERIFYING"
                    delay(1500)
                    workState = "VERIFIED"
                    delay(1000)
                    workState = "ACTIVATED"
                    viewModel.startWork()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = workState == "READY" && !isWorking
        ) {
            Text(if (isWorking) "WORK VERIFIED & ACTIVE" else "START WORK", color = androidx.compose.ui.graphics.Color(0xFF0A0A0A), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatusRow(icon: ImageVector, title: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextMuted, fontSize = 14.sp)
            Text(text = value, color = valueColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
