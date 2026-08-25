package com.example.gigshield.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gigshield.theme.DeepBase
import com.example.gigshield.theme.ElectricBlue
import com.example.gigshield.theme.TextHighContrast

@Composable
fun HelpSupportScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(16.dp)
    ) {
        Text("Help & Support", style = MaterialTheme.typography.headlineLarge, color = TextHighContrast)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Frequently Asked Questions", style = MaterialTheme.typography.titleLarge, color = ElectricBlue)
        Spacer(modifier = Modifier.height(8.dp))
        Text("- How does daily insurance work?", color = TextHighContrast)
        Text("- How is work verified?", color = TextHighContrast)
        Text("- Why did my insurance activate?", color = TextHighContrast)
        Text("- How do I submit a claim?", color = TextHighContrast)
        Text("- How long does claim review take?", color = TextHighContrast)
        Text("- How is my premium calculated?", color = TextHighContrast)
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
