package com.example.gigshield.ui.score

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.*

@Composable
fun ScoreboardScreen(
    viewModel: ScoreboardViewModel,
    onNavigateHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBase)
            .padding(20.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "EARNINGS",
                style = MaterialTheme.typography.labelLarge,
                color = TextMuted
            )
            // Profile icon placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevated)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Weekly Earnings Hero
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "This Week's Earnings",
                style = MaterialTheme.typography.labelLarge,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            // The Electric Blue glow could be done with advanced modifiers, but we'll use a tinted text shadow or just the color
            Text(
                text = "USD 124.50",
                style = MaterialTheme.typography.displayLarge,
                color = ElectricBlue
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Safe Rider Rewards Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard)
                .border(1.dp, SurfaceElevated, RoundedCornerShape(12.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Safe Rider Rewards",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextHighContrast
                    )
                    Surface(
                        color = DeepBase,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${uiState.streakCount} Day Streak \uD83D\uDD25",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = TextHighContrast
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Progress Bar
                LinearProgressIndicator(
                    progress = { 3f / 5f }, // hardcoded for mockup
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = SafetyGreen,
                    trackColor = DeepBase
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "3/5 trips to next free premium day",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Recent Trips",
            style = MaterialTheme.typography.titleLarge,
            color = TextHighContrast
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Recent Trips List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(5) { index ->
                TripItem(
                    distance = "${5.4 + index} km",
                    duration = "${12 + index * 2} min",
                    amount = "USD ${8.20 + index}"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceCard,
                contentColor = TextHighContrast
            ),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceElevated)
        ) {
            Text("Back to Dashboard", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun TripItem(distance: String, duration: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepBase)
            .border(1.dp, SurfaceElevated, RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = distance, style = MaterialTheme.typography.titleLarge, color = TextHighContrast, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = duration, style = MaterialTheme.typography.bodyLarge, color = TextMuted, fontSize = 14.sp)
        }
        Text(text = amount, style = MaterialTheme.typography.titleLarge, color = SafetyGreen, fontSize = 18.sp)
    }
}
