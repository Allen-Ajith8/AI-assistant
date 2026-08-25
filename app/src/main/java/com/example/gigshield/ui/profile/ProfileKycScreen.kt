package com.example.gigshield.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileKycScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile & KYC", color = TextHighContrast) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBase)
            )
        },
        containerColor = DeepBase
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = SurfaceCard
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Photo",
                        tint = TextMuted,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
            
            SectionCard(title = "Personal Details") {
                DetailRow("Name", "John Doe")
                DetailRow("Phone", "+1 234 567 8900")
                DetailRow("Email", "john.doe@example.com")
                DetailRow("Occupation", "Delivery Driver")
                DetailRow("Gig Platform", "Uber Eats")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SectionCard(title = "Identity Verification") {
                DetailRow("Status", "VERIFIED", ElectricBlue)
                DetailRow("Document Type", "Driver's License")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SectionCard(title = "Bank & Payout Details") {
                DetailRow("Bank Name", "Chase Bank")
                DetailRow("Account Number", "****1234")
            }
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = TextHighContrast, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color = TextHighContrast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = valueColor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
