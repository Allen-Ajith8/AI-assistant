package com.example.gigshield.ui.permissions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

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
            TopAppBar(
                title = { Text("System Check", color = TextHighContrast) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBase)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = DeepBase) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    enabled = allGranted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SafetyGreen,
                        contentColor = DeepBase,
                        disabledContainerColor = SurfaceElevated,
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("START SHIFT", style = MaterialTheme.typography.labelLarge)
                }
            }
        },
        containerColor = DeepBase
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "GigGuard needs these permissions to provide live coverage.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(24.dp))

            PermissionItem(
                title = "Location Tracking",
                description = "Required to calculate trip distance accurately.",
                isGranted = locGranted,
                onGrant = { locGranted = true }
            )

            PermissionItem(
                title = "Motion Sensors",
                description = "Required for Safe Rider Score and crash detection.",
                isGranted = motionGranted,
                onGrant = { motionGranted = true }
            )

            PermissionItem(
                title = "Background Execution",
                description = "Prevents the OS from killing the app mid-shift.",
                isGranted = batteryGranted,
                onGrant = { batteryGranted = true }
            )

            PermissionItem(
                title = "Auto-Start (iQOO/Vivo)",
                description = "Required to keep the telematics engine running.",
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCard)
            .border(
                1.dp,
                if (isGranted) SafetyGreen.copy(alpha = 0.5f) else SurfaceElevated,
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Outlined.Info,
                        contentDescription = null,
                        tint = if (isGranted) SafetyGreen else ElectricBlue
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextHighContrast
                    )
                }
                if (!isGranted) {
                    OutlinedButton(
                        onClick = onGrant,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue)
                    ) {
                        Text("Grant")
                    }
                } else {
                    Text(
                        text = "READY",
                        color = SafetyGreen,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted
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
