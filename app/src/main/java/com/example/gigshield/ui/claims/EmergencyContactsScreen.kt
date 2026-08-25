package com.example.gigshield.ui.claims

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Contacts", color = TextHighContrast) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Add Contact */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Contact", tint = TextHighContrast)
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
                .padding(16.dp)
        ) {
            ContactCard("John Doe", "Spouse", "+1 234 567 8900", true)
            Spacer(modifier = Modifier.height(16.dp))
            ContactCard("Jane Smith", "Parent", "+1 987 654 3210", false)
        }
    }
}

@Composable
fun ContactCard(name: String, relation: String, phone: String, isPrimary: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(name, color = TextHighContrast, style = MaterialTheme.typography.titleMedium)
                if (isPrimary) {
                    Text("PRIMARY", color = ElectricBlue, style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(relation, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(phone, color = TextHighContrast, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { /* TODO */ }) { Text("SET PRIMARY", color = ElectricBlue) }
                TextButton(onClick = { /* TODO */ }) { Text("EDIT", color = ElectricBlue) }
                TextButton(onClick = { /* TODO */ }) { Text("DELETE", color = ErrorRed) }
            }
        }
    }
}
