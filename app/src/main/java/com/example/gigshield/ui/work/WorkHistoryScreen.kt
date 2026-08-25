package com.example.gigshield.ui.work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.*

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.gigshield.ui.SharedViewModel

@Composable
fun WorkHistoryScreen(viewModel: SharedViewModel, onBack: () -> Unit) {
    val history by viewModel.workHistory.collectAsState()

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
                text = "Work History",
                color = TextHighContrast,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { }) {
                Icon(Icons.Default.DateRange, contentDescription = "Filter Date", tint = ElectricBlue)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(history.size) { index ->
                val session = history[index]
                WorkHistoryCard(
                    date = session.date,
                    timeRange = "09:00 - 18:00",
                    duration = session.duration,
                    verification = if (session.verified) "VERIFIED" else "PENDING",
                    premium = session.premium
                )
            }
        }
    }
}

@Composable
fun WorkHistoryCard(date: String, timeRange: String, duration: String, verification: String, premium: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = date, color = TextHighContrast, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = verification, color = SafetyGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Time: $timeRange", color = TextMuted, fontSize = 14.sp)
                Text(text = "Premium: $premium", color = ElectricBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Duration: $duration", color = TextMuted, fontSize = 14.sp)
        }
    }
}
