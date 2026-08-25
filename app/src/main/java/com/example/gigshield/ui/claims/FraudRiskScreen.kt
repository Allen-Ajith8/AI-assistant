package com.example.gigshield.ui.claims

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FraudRiskScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fraud Risk Analysis", color = TextHighContrast) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBase)
            )
        },
        containerColor = DeepBase
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Risk Score", color = TextMuted)
                    Text("LOW", color = SafetyGreen, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Signals", color = TextHighContrast, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            SignalItem("Duplicate claim", "LOW", SafetyGreen)
            SignalItem("Timestamp consistency", "GOOD", SafetyGreen)
            SignalItem("Policy activity", "VERIFIED", SafetyGreen)
        }
    }
}

@Composable
fun SignalItem(label: String, status: String, statusColor: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextHighContrast)
        Text(status, color = statusColor, fontWeight = FontWeight.Bold)
    }
}
