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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.data.model.InsuranceTier
import com.example.gigshield.theme.*
import com.example.gigshield.ui.components.GradientWaveBackground
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToPlanSelection: () -> Unit,
    onNavigateToScoreboard: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onToggleOnline = { viewModel.toggleOnlineStatus() },
        onDismissSummary = { viewModel.dismissSummary() },
        onNavigateToPlanSelection = onNavigateToPlanSelection,
        onNavigateToScoreboard = onNavigateToScoreboard,
        onNavigateToPermissions = onNavigateToPermissions,
        onOpenDrawer = onOpenDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: UiState,
    onToggleOnline: () -> Unit,
    onDismissSummary: () -> Unit,
    onNavigateToPlanSelection: () -> Unit,
    onNavigateToScoreboard: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    // Shift Summary Bottom Sheet
    if (uiState.showSummary && uiState.shiftSummary != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissSummary,
            containerColor = SurfaceCard,
            contentColor = TextHighContrast,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(TextMuted)
                )
            }
        ) {
            ShiftSummaryContent(
                summary = uiState.shiftSummary,
                tierName = uiState.currentTier?.displayName ?: "No Plan",
                onDismiss = onDismissSummary
            )
        }
    }

    GradientWaveBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Greeting + Date Header
            val greeting = remember {
                val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
                when {
                    hour < 12 -> "Good morning"
                    hour < 17 -> "Good afternoon"
                    else -> "Good evening"
                }
            }
            val dateText = remember {
                SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date())
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onOpenDrawer, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = TextHighContrast
                        )
                    }
                    Column {
                        Text(
                            text = "$greeting, Rider",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextHighContrast
                        )
                        Text(
                            text = dateText,
                            style = MaterialTheme.typography.labelMedium,
                            color = TextMuted
                        )
                    }
                }
                // Profile icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SurfaceElevated)
                        .border(2.dp, ElectricBlue.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("R", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Status Chip
            Surface(
                color = if (uiState.isOnline) SafetyGreen.copy(alpha = 0.15f) else SurfaceElevated.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (uiState.isOnline) SafetyGreen.copy(alpha = 0.5f) else Color.Transparent
                )
            ) {
                Text(
                    text = if (uiState.isOnline) "● SHIFT ACTIVE" else "○ OFFLINE",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (uiState.isOnline) SafetyGreen else TextMuted,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                text = timeText,
                style = MaterialTheme.typography.displayLarge,
                color = TextHighContrast,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Toggle Button
            val buttonColor by animateColorAsState(
                targetValue = if (uiState.isOnline) SafetyGreen else SurfaceElevated,
                label = "buttonColor"
            )

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(if (uiState.isOnline) SafetyGreen.copy(alpha = 0.08f) else ElectricBlue.copy(alpha = 0.05f))
                    .clickable(onClick = onToggleOnline),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
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
                        Icon(
                            imageVector = if (uiState.isOnline) Icons.Default.Timer else Icons.Default.Shield,
                            contentDescription = null,
                            tint = if (uiState.isOnline) DeepBase else ElectricBlue,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (uiState.isOnline) "END SHIFT" else "GO ONLINE",
                            color = if (uiState.isOnline) DeepBase else ElectricBlue,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Coverage Banner
            uiState.currentTier?.let { tier ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCard.copy(alpha = 0.7f))
                        .border(1.dp, if (uiState.isOnline) SafetyGreen.copy(alpha = 0.3f) else SurfaceElevated, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = tier.displayName.uppercase(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = SafetyGreen
                                )
                                Text(
                                    text = if (uiState.isOnline) "Coverage Active" else "Ready to Activate",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextMuted,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = "₹${tier.dailyPremium}/day",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextHighContrast
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = SurfaceElevated.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Coverage details strip
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CoverageMini("Death", "₹5L")
                            CoverageMini("Hospital", "₹50K")
                            CoverageMini("Liability", "₹50K")
                            if (tier == InsuranceTier.INCOME_PROTECTOR) {
                                CoverageMini("EMI", "Waiver")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = if (uiState.isOnline) "DISTANCE" else "THIS WEEK",
                    value = if (uiState.isOnline) String.format("%.1f km", uiState.distanceKm) else "${uiState.weeklyShiftCount} shifts",
                    icon = Icons.Default.DirectionsBike,
                    accentColor = ElectricBlue
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = if (uiState.isOnline) "EARNINGS" else "REWARDS",
                    value = if (uiState.isOnline) String.format("₹%.0f", uiState.currentEarnings) else "₹${uiState.earnedRewards}",
                    icon = Icons.Default.MonetizationOn,
                    accentColor = SafetyGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Chart
            val chartData = remember { listOf(150f, 220f, 180f, 310f, 290f, 400f, uiState.currentEarnings) }
            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            
            com.example.gigshield.ui.components.WeeklyEarningsChart(
                dataPoints = chartData,
                daysOfWeek = days,
                lineColor = SafetyGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Safe Rider Score Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard.copy(alpha = 0.7f))
                    .border(1.dp, SurfaceElevated, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = SurfaceElevated,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = SafetyGreen,
                            startAngle = -90f,
                            sweepAngle = 360f * ((uiState.todayScore ?: 0) / 100f),
                            useCenter = false,
                            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${uiState.todayScore ?: 0}",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextHighContrast,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Safe Rider Score",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextHighContrast,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (uiState.isOnline) "Live tracking • ${uiState.streakDays} day streak 🔥"
                        else "Keep riding safe • ${uiState.streakDays} day streak 🔥",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (uiState.isOnline) SafetyGreen else TextMuted
                    )
                }
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = SafetyGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Quick Actions Row
            if (!uiState.isOnline) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToPlanSelection,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Plans", fontSize = 13.sp)
                    }
                    OutlinedButton(
                        onClick = onNavigateToScoreboard,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SafetyGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SafetyGreen.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Earnings", fontSize = 13.sp)
                    }
                    OutlinedButton(
                        onClick = onNavigateToPermissions,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceElevated),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Settings", fontSize = 13.sp)
                    }
                }
            } else {
                // End Shift button when online
                Button(
                    onClick = onToggleOnline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed.copy(alpha = 0.15f),
                        contentColor = ErrorRed
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.4f))
                ) {
                    Text("End Shift", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// --- Reusable Sub-Components ---

@Composable
fun CoverageMini(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.labelLarge, color = ElectricBlue, fontSize = 13.sp)
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = TextMuted, fontSize = 10.sp)
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard.copy(alpha = 0.7f))
            .border(1.dp, SurfaceElevated, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = label, style = MaterialTheme.typography.labelMedium, color = TextMuted, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, color = TextHighContrast, fontSize = 20.sp)
        }
    }
}

@Composable
fun ShiftSummaryContent(
    summary: ShiftSummary,
    tierName: String,
    onDismiss: () -> Unit
) {
    val hours = summary.totalTimeMs / 3_600_000
    val minutes = (summary.totalTimeMs % 3_600_000) / 60_000
    val seconds = (summary.totalTimeMs % 60_000) / 1_000

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Shift Complete", style = MaterialTheme.typography.titleLarge, color = SafetyGreen, fontWeight = FontWeight.Bold)
        Text(tierName, style = MaterialTheme.typography.labelMedium, color = TextMuted)

        Spacer(modifier = Modifier.height(24.dp))

        // Duration hero
        Text(
            text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
            style = MaterialTheme.typography.displayLarge,
            color = TextHighContrast,
            letterSpacing = 2.sp
        )
        Text("Total Shift Time", style = MaterialTheme.typography.labelMedium, color = TextMuted)

        Spacer(modifier = Modifier.height(24.dp))

        // Stats grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryMetric("Distance", String.format("%.1f km", summary.distanceKm), ElectricBlue)
            SummaryMetric("Earnings", String.format("₹%.0f", summary.estimatedEarnings), SafetyGreen)
            SummaryMetric("Score", "${summary.safeRiderScore}", SafetyGreen)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Driving events breakdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DeepBase)
                .padding(16.dp)
        ) {
            Column {
                Text("Driving Events", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                Spacer(modifier = Modifier.height(12.dp))
                EventRow("Harsh Braking", summary.harshBrakingCount, ErrorRed)
                EventRow("Rapid Acceleration", summary.rapidAccelCount, Color(0xFFFFD740))
                EventRow("Sharp Cornering", summary.sharpCorneringCount, ElectricBlue)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Top Speed", style = MaterialTheme.typography.bodyLarge, color = TextMuted, fontSize = 14.sp)
                    Text(String.format("%.0f km/h", summary.topSpeedKmh), style = MaterialTheme.typography.labelLarge, color = TextHighContrast)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SafetyGreen, contentColor = androidx.compose.ui.graphics.Color(0xFF0A0A0A)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("DONE", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SummaryMetric(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, color = color, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = TextMuted)
    }
}

@Composable
fun EventRow(label: String, count: Int, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge, color = TextMuted, fontSize = 14.sp)
        }
        Text(text = "$count", style = MaterialTheme.typography.labelLarge, color = TextHighContrast)
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
                earnedRewards = 45,
                distanceKm = 18.4f,
                currentEarnings = 248f
            ),
            onToggleOnline = {},
            onDismissSummary = {},
            onNavigateToPlanSelection = {},
            onNavigateToScoreboard = {},
            onNavigateToPermissions = {},
            onOpenDrawer = {}
        )
    }
}
