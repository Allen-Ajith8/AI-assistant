package com.example.gigshield.ui.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.GigShieldTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    var locGranted by remember { mutableStateOf(false) }
    var motionGranted by remember { mutableStateOf(false) }
    var batteryGranted by remember { mutableStateOf(false) }
    var autostartGranted by remember { mutableStateOf(false) }

    val allGranted = locGranted && motionGranted && batteryGranted && autostartGranted

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Required Permissions") })
        },
        bottomBar = {
            BottomAppBar(containerColor = MaterialTheme.colorScheme.background) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    enabled = allGranted
                ) {
                    Text("Continue")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "GigShield needs these permissions to provide insurance coverage during your shifts.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            PermissionItem(
                title = "Location (Fine + Background)",
                description = "Required to track shift distance and driving behavior.",
                isGranted = locGranted,
                onGrant = { locGranted = true }
            )

            PermissionItem(
                title = "Motion Sensors",
                description = "Required to detect harsh braking and acceleration.",
                isGranted = motionGranted,
                onGrant = { motionGranted = true }
            )

            PermissionItem(
                title = "Battery Optimization",
                description = "Allows the app to run reliably in the background.",
                isGranted = batteryGranted,
                onGrant = { batteryGranted = true }
            )

            PermissionItem(
                title = "Auto-start",
                description = "Required for iQOO devices to ensure the app isn't killed.",
                isGranted = autostartGranted,
                onGrant = { autostartGranted = true }
            )
        }
    }
}

@Composable
fun PermissionItem(
    title: String,
    description: String,
    isGranted: Boolean,
    onGrant: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Outlined.Info,
                        contentDescription = null,
                        tint = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (!isGranted) {
                    Button(onClick = onGrant) {
                        Text("Grant")
                    }
                } else {
                    Text(
                        text = "Granted",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun PreviewPermissionsScreen() {
    GigShieldTheme {
        PermissionsScreen(onBack = {}, onContinue = {})
    }
}
