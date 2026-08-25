package com.example.gigshield.ui.work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.*

@Composable
fun GigPlatformScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
            }
            Text(
                text = "Gig Platforms",
                color = TextHighContrast,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PlatformCard(name = "Zomato", status = "CONNECTED", statusColor = SafetyGreen, actionText = "SYNC NOW")
        Spacer(modifier = Modifier.height(12.dp))
        PlatformCard(name = "Uber", status = "SYNCING", statusColor = ElectricBlue, actionText = "DISCONNECT")
        Spacer(modifier = Modifier.height(12.dp))
        PlatformCard(name = "Blinkit", status = "NOT CONNECTED", statusColor = ErrorRed, actionText = "CONNECT")
    }
}

@Composable
fun PlatformCard(name: String, status: String, statusColor: androidx.compose.ui.graphics.Color, actionText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, color = TextHighContrast, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = status, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (actionText == "CONNECT") ElectricBlue else DeepBase,
                    contentColor = if (actionText == "CONNECT") DeepBase else ElectricBlue
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = actionText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
