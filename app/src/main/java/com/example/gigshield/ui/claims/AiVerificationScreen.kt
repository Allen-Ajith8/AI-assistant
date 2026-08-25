package com.example.gigshield.ui.claims

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiVerificationScreen(onBack: () -> Unit) {
    val checks = listOf(
        "Policy active",
        "Work session verified",
        "Incident within work period",
        "Evidence available",
        "Duplicate claim (No duplicate)"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Verification", color = TextHighContrast) },
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
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CLAIM VERIFICATION SCORE", color = TextMuted)
                        Spacer(Modifier.height(8.dp))
                        Text("92%", color = ElectricBlue, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Text("Risk: LOW", color = SafetyGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Text("Automated Checks", color = TextHighContrast, fontWeight = FontWeight.Bold)
            }

            items(checks.size) { index ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = SafetyGreen)
                    Spacer(Modifier.width(12.dp))
                    Text(checks[index], color = TextHighContrast)
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Automated verification is a recommendation for insurer review. Final approval depends on insurer policies.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
