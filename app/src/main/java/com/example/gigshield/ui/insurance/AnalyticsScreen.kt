package com.example.gigshield.ui.insurance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", color = TextHighContrast) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBase)
            )
        },
        containerColor = DeepBase
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(modifier = Modifier.weight(1f), title = "Active Workdays", value = "21")
                    StatCard(modifier = Modifier.weight(1f), title = "Total Hours", value = "164")
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(modifier = Modifier.weight(1f), title = "Total Premium", value = "₹525")
                    StatCard(modifier = Modifier.weight(1f), title = "Coverage Days", value = "21")
                }
            }
            item {
                StatCard(modifier = Modifier.fillMaxWidth(), title = "Claims", value = "0")
            }
            item {
                Text("Charts & Insights", color = TextHighContrast, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                com.example.gigshield.ui.components.WeeklyEarningsChart(
                    dataPoints = listOf(150f, 220f, 180f, 310f, 290f, 400f, 250f),
                    daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
                    lineColor = ElectricBlue,
                    fillColor = ElectricBlue.copy(alpha = 0.2f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                com.example.gigshield.ui.components.WeeklyEarningsChart(
                    dataPoints = listOf(5f, 6.5f, 4f, 8f, 7.5f, 9f, 6f),
                    daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
                    lineColor = SafetyGreen,
                    fillColor = SafetyGreen.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = TextMuted, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = ElectricBlue, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
