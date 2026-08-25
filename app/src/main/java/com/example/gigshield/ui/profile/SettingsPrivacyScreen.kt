package com.example.gigshield.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPrivacyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings & Privacy", color = TextHighContrast) },
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
            SettingsCategory(title = "ACCOUNT") {
                SettingsItem("Notifications")
                SettingsItem("Language")
                SettingsItem("Theme")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SettingsCategory(title = "PERMISSIONS") {
                SettingsItem("Location", "Granted")
                SettingsItem("Motion/Activity", "Granted")
                SettingsItem("Microphone", "Denied")
                SettingsItem("Camera", "Granted")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SettingsCategory(title = "PRIVACY") {
                SettingsItem("Privacy Policy")
                SettingsItem("Data Usage")
                SettingsItem("Delete Account", isDestructive = true)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SettingsCategory(title = "SECURITY") {
                SettingsItem("Change Password")
                SettingsItem("Logout", isDestructive = true)
            }
        }
    }
}

@Composable
fun SettingsCategory(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            color = TextMuted,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, value: String? = null, isDestructive: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = if (isDestructive) ErrorRed else TextHighContrast,
            style = MaterialTheme.typography.bodyLarge
        )
        if (value != null) {
            Text(
                text = value,
                color = TextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
