package com.example.gigshield.ui.claims

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.gigshield.ui.SharedViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyClaimsScreen(viewModel: SharedViewModel, onBack: () -> Unit) {
    val claims by viewModel.claims.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Claims", color = TextHighContrast) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(claims.size) { index ->
                val claim = claims[index]
                ClaimCard(claim.id, claim.date, claim.type, claim.amount, claim.status)
            }
        }
    }
}

@Composable
fun ClaimCard(id: String, date: String, type: String, amount: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(id, color = TextHighContrast, fontWeight = FontWeight.Bold)
                Text(amount, color = ElectricBlue, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Type: $type", color = TextMuted)
            Text("Date: $date", color = TextMuted)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Status: $status", color = SafetyGreen, fontWeight = FontWeight.SemiBold)
        }
    }
}
