package com.example.gigshield.ui.garage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.DeepBase
import com.example.gigshield.theme.ElectricBlue
import com.example.gigshield.theme.TextHighContrast

@Composable
fun VehicleGarageScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Vehicle Garage", style = MaterialTheme.typography.displaySmall, color = TextHighContrast)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Upload your DL, RC, and Pre-Shift Condition photos here.", color = TextHighContrast)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
        ) {
            Text("Back")
        }
    }
}
