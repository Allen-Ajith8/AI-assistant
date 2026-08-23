package com.example.gigshield.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gigshield.data.model.InsuranceTier
import com.example.gigshield.theme.GigShieldTheme
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToPlanSelection: () -> Unit,
    onNavigateToScoreboard: () -> Unit,
    onNavigateToPermissions: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    HomeScreenContent(
        uiState = uiState,
        onToggleOnline = { viewModel.toggleOnlineStatus() },
        onNavigateToPlanSelection = onNavigateToPlanSelection,
        onNavigateToScoreboard = onNavigateToScoreboard,
        onNavigateToPermissions = onNavigateToPermissions
    )
}

@Composable
fun HomeScreenContent(
    uiState: UiState,
    onToggleOnline: () -> Unit,
    onNavigateToPlanSelection: () -> Unit,
    onNavigateToScoreboard: () -> Unit,
    onNavigateToPermissions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Timer
        var timeText by remember { mutableStateOf("00:00:00") }
        LaunchedEffect(uiState.isOnline, uiState.shiftStartTime) {
            if (uiState.isOnline && uiState.shiftStartTime != null) {
                while (true) {
                    val diff = (System.currentTimeMillis() - uiState.shiftStartTime) / 1000
                    val h = diff / 3600
                    val m = (diff % 3600) / 60
                    val s = diff % 60
                    timeText = String.format("%02d:%02d:%02d", h, m, s)
                    delay(1000)
                }
            } else {
                timeText = "00:00:00"
            }
        }
        
        Text(
            text = if (uiState.isOnline) timeText else "OFFLINE",
            style = MaterialTheme.typography.displayMedium,
            color = if (uiState.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Toggle Button
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    if (uiState.isOnline) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .clickable(onClick = onToggleOnline),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(
                        if (uiState.isOnline) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (uiState.isOnline) "ON THE CLOCK" else "TAP TO START SHIFT",
                    color = if (uiState.isOnline) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        uiState.currentTier?.let { tier ->
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "${tier.displayName} · ₹${tier.dailyPremium}/day",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Score Indicator
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            val scoreColor = MaterialTheme.colorScheme.primary
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.DarkGray,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = scoreColor,
                    startAngle = 135f,
                    sweepAngle = 270f * ((uiState.todayScore ?: 0) / 100f),
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${uiState.todayScore ?: 0}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(value = "${uiState.weeklyShiftCount}", label = "Shifts")
            StatItem(value = "🔥${uiState.streakDays}", label = "Streak")
            StatItem(value = "★${uiState.earnedRewards}", label = "Rewards")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = onNavigateToPlanSelection) {
                Text("Change Plan")
            }
            Button(onClick = onNavigateToScoreboard) {
                Text("View Scores")
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    GigShieldTheme {
        HomeScreenContent(
            uiState = UiState(),
            onToggleOnline = {},
            onNavigateToPlanSelection = {},
            onNavigateToScoreboard = {},
            onNavigateToPermissions = {}
        )
    }
}
