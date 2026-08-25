package com.example.gigshield.ui.insurance

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
fun DailyPremiumScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Premium", color = TextHighContrast) },
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("TODAY'S PREMIUM", color = TextMuted)
                        Text("₹25", color = ElectricBlue, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("You pay only for verified active workdays.", color = TextMuted)
                    }
                }
            }
            item {
                Text("Status", color = TextHighContrast, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Today's Status: Active", color = SafetyGreen)
                        Text("Premium Status: Deducted", color = TextMuted)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total Premium this Month: ₹525", color = TextHighContrast)
                        Text("Active Days this Month: 21", color = TextHighContrast)
                    }
                }
            }
            item {
                Text("Premium History Summary", color = TextHighContrast, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                PremiumHistoryItem("25 Aug 2026", "₹25", "SUCCESS")
                PremiumHistoryItem("24 Aug 2026", "₹25", "SUCCESS")
                PremiumHistoryItem("23 Aug 2026", "₹25", "SUCCESS")
            }
        }
    }
}

@Composable
fun PremiumHistoryItem(date: String, amount: String, status: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(date, color = TextHighContrast)
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(amount, color = ElectricBlue, fontWeight = FontWeight.Bold)
                Text(status, color = SafetyGreen, fontSize = 12.sp)
            }
        }
    }
}
