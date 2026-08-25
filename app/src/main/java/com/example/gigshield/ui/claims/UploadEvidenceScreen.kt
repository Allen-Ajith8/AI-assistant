package com.example.gigshield.ui.claims

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadEvidenceScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Evidence", color = TextHighContrast) },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = SurfaceCard)) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = ElectricBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("Camera", color = TextHighContrast)
                }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = SurfaceCard)) {
                    Icon(Icons.Default.Image, contentDescription = "Gallery", tint = ElectricBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("Gallery", color = TextHighContrast)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceCard)
            ) {
                Icon(Icons.Default.InsertDriveFile, contentDescription = "Documents", tint = ElectricBlue)
                Spacer(Modifier.width(8.dp))
                Text("Browse Documents", color = TextHighContrast)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Uploaded Files", color = TextHighContrast, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("accident_photo.jpg", color = TextHighContrast)
                        Text("Ready", color = SafetyGreen, style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Submit Evidence", color = androidx.compose.ui.graphics.Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
            }
        }
    }
}
