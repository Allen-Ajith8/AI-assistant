package com.example.gigshield.ui.work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.*

@Composable
fun WorkActivityScreen(onBack: () -> Unit) {
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
                text = "Real-time Work Activity",
                color = TextHighContrast,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActivityCard(modifier = Modifier.weight(1f), title = "WORK STATUS", value = "ACTIVE", color = SafetyGreen)
            ActivityCard(modifier = Modifier.weight(1f), title = "GIG PLATFORM", value = "ONLINE", color = ElectricBlue)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActivityCard(modifier = Modifier.weight(1f), title = "LOCATION", value = "AVAILABLE", color = SafetyGreen)
            ActivityCard(modifier = Modifier.weight(1f), title = "ACTIVITY", value = "DETECTED", color = SafetyGreen)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "VERIFICATION SCORE", color = TextMuted, fontSize = 14.sp)
                Text(text = "87%", color = ElectricBlue, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { 0.87f },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = ElectricBlue,
                    trackColor = DeepBase
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Work activity is verified using permitted device, location and gig-platform signals.",
            color = TextMuted,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ActivityCard(modifier: Modifier = Modifier, title: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = TextMuted, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
