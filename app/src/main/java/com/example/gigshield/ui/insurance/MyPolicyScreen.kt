package com.example.gigshield.ui.insurance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Policy", color = TextHighContrast) },
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ACTIVE", color = SafetyGreen, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Policy Number: GIG-123456789", color = TextHighContrast)
                        Text("Plan Name: Daily Shield Pro", color = TextHighContrast)
                        Text("Start Date: 01 Aug 2026", color = TextMuted)
                        Text("Active Days: 21", color = TextMuted)
                        Text("Premium Paid: ₹525", color = TextMuted)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Coverage Amount: ₹2,00,000", color = ElectricBlue, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            item {
                Text("Details", color = TextHighContrast, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                PolicyDetailItem("Coverage", "Accident, Health Support")
                PolicyDetailItem("Exclusions", "Pre-existing conditions")
                PolicyDetailItem("Terms", "Valid only on active workdays")
            }
        }
    }
}

@Composable
fun PolicyDetailItem(title: String, detail: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = ElectricBlue, fontWeight = FontWeight.SemiBold)
            Text(detail, color = TextMuted)
        }
    }
}
