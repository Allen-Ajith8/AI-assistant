package com.example.gigshield.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.example.gigshield.data.model.InsuranceTier
import com.example.gigshield.theme.*
import com.example.gigshield.ui.components.GradientWaveBackground
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
    GradientWaveBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Top App Bar Area
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GigGuard",
                style = MaterialTheme.typography.titleLarge,
                color = TextHighContrast
            )
            // Profile icon placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevated)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
        
        // Timer Display
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
            text = if (uiState.isOnline) "SHIFT ACTIVE" else "OFFLINE",
            style = MaterialTheme.typography.labelLarge,
            color = if (uiState.isOnline) SafetyGreen else TextMuted
        )
        Text(
            text = timeText,
            style = MaterialTheme.typography.displayLarge,
            color = TextHighContrast
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        // Main Toggle Button (Kinetic Shield Design)
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (uiState.isOnline) 1.05f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
        
        val buttonColor by animateColorAsState(
            targetValue = if (uiState.isOnline) SafetyGreen else SurfaceElevated,
            label = "buttonColor"
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .background(if (uiState.isOnline) SafetyGreen.copy(alpha = 0.1f) else ElectricBlue.copy(alpha = 0.05f))
                .clickable(onClick = onToggleOnline),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(buttonColor)
                    .border(
                        width = 2.dp,
                        color = if (uiState.isOnline) SafetyGreen else ElectricBlue.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (uiState.isOnline) "GO OFFLINE" else "GO ONLINE",
                        color = if (uiState.isOnline) DeepBase else ElectricBlue,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Active Premium Protection Banner
        uiState.currentTier?.let { tier ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCard.copy(alpha = 0.7f))
                    .border(1.dp, SurfaceElevated, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = tier.displayName.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = SafetyGreen
                    )
                    Text(
                        text = "Premium Protection Active",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "₹${tier.dailyPremium}/day",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextHighContrast
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Stats Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Metric
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCard.copy(alpha = 0.7f))
                    .border(1.dp, SurfaceElevated, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = if (uiState.isOnline) "DISTANCE" else "WEEKLY SHIFTS", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = if (uiState.isOnline) "18.4 km" else "${uiState.weeklyShiftCount}", style = MaterialTheme.typography.titleLarge, color = TextHighContrast)
                }
            }
            
            // Right Metric
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCard.copy(alpha = 0.7f))
                    .border(1.dp, SurfaceElevated, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = if (uiState.isOnline) "CURRENT EARNINGS" else "REWARDS", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = if (uiState.isOnline) "₹ 248.50" else "₹ ${uiState.earnedRewards}", style = MaterialTheme.typography.titleLarge, color = ElectricBlue)
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Safety / Score Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceCard.copy(alpha = 0.7f))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = SurfaceElevated,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = SafetyGreen,
                        startAngle = -90f,
                        sweepAngle = 360f * ((uiState.todayScore ?: 0) / 100f),
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "${uiState.todayScore ?: 0}",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextHighContrast
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Safe Rider Score", style = MaterialTheme.typography.bodyLarge, color = TextHighContrast, fontWeight = FontWeight.Bold)
                if (uiState.isOnline) {
                    Text(text = "Live tracking active", style = MaterialTheme.typography.labelMedium, color = SafetyGreen)
                } else {
                    Text(text = "Excellent driving today", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Bottom Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (!uiState.isOnline) {
                OutlinedButton(
                    onClick = onNavigateToPlanSelection,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextHighContrast),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceElevated)
                ) {
                    Text("Change Plan")
                }
            } else {
                OutlinedButton(
                    onClick = onToggleOnline,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("End Shift")
                }
            }
        }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    GigShieldTheme {
        HomeScreenContent(
            uiState = UiState(
                isOnline = true,
                currentTier = InsuranceTier.INCOME_PROTECTOR,
                todayScore = 88,
                weeklyShiftCount = 12,
                streakDays = 5,
                earnedRewards = 45
            ),
            onToggleOnline = {},
            onNavigateToPlanSelection = {},
            onNavigateToScoreboard = {},
            onNavigateToPermissions = {}
        )
    }
}
