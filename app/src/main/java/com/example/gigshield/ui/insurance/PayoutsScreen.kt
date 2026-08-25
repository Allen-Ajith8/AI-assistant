package com.example.gigshield.ui.insurance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun PayoutsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payout History", color = TextHighContrast) },
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
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Claim #GS1024", color = TextHighContrast, fontWeight = FontWeight.Bold)
                        Text("₹50,000", color = ElectricBlue, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Status: PROCESSING", color = SafetyGreen, fontWeight = FontWeight.SemiBold)
                    Text("Reference: DEMO-123", color = TextMuted)
                }
            }
        }
    }
}
