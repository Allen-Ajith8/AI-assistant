package com.example.gigshield.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", color = TextHighContrast) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Mark all as read */ }) {
                        Icon(Icons.Default.DoneAll, contentDescription = "Mark all as read", tint = TextHighContrast)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBase)
            )
        },
        containerColor = DeepBase
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { NotificationCard("Work Verified", "Your gig work has been successfully verified.", "10 min ago") }
            item { NotificationCard("Insurance Activated", "Your micro-insurance policy is now active.", "1 hour ago") }
            item { NotificationCard("Premium Charged", "A premium of $2.50 has been charged.", "2 hours ago") }
            item { NotificationCard("Claim Submitted", "Your claim #12345 has been submitted.", "1 day ago") }
        }
    }
}

@Composable
fun NotificationCard(title: String, message: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, color = TextHighContrast, style = MaterialTheme.typography.titleMedium)
                Text(time, color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { /* TODO */ }) {
                    Text("MARK AS READ", color = ElectricBlue)
                }
            }
        }
    }
}
