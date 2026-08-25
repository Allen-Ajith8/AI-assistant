package com.example.gigshield.ui.score

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.gigshield.ui.components.GradientWaveBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardScreen(
    viewModel: ScoreboardViewModel,
    onNavigateHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    GradientWaveBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Top App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateHome) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextHighContrast)
                }
                Text(
                    text = "EARNINGS",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                Text(
                    text = "₹ 1,245",
                    style = MaterialTheme.typography.displayLarge,
                    color = ElectricBlue,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "from ${uiState.history.size} shifts",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Weekly Score Chart (horizontal bars)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard.copy(alpha = 0.7f))
                    .border(1.dp, SurfaceElevated, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("Weekly Score Trend", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                    Spacer(modifier = Modifier.height(12.dp))

                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    uiState.history.forEachIndexed { index, score ->
                        val dayLabel = days.getOrElse(index) { "—" }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dayLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = TextMuted,
                                modifier = Modifier.width(36.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DeepBase)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(fraction = score / 100f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            when {
                                                score >= 80 -> SafetyGreen
                                                score >= 60 -> ElectricBlue
                                                else -> ErrorRed
                                            }
                                        )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$score",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextHighContrast,
                                modifier = Modifier.width(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Safe Rider Rewards Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard.copy(alpha = 0.7f))
                    .border(1.dp, SurfaceElevated, RoundedCornerShape(12.dp))
                    .padding(16.dp)
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
                            color = TextHighContrast,
                            fontSize = 18.sp
                        )
                        Surface(
                            color = androidx.compose.ui.graphics.Color(0xFF0A0A0A),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${uiState.streakCount} Day Streak 🔥",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = TextHighContrast
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { 3f / 5f },
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
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Recent Trips",
                style = MaterialTheme.typography.titleLarge,
                color = TextHighContrast,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Recent Trips List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                val trips = listOf(
                    Triple("12.3 km", "28 min", "₹ 185"),
                    Triple("8.7 km", "18 min", "₹ 130"),
                    Triple("5.1 km", "11 min", "₹ 76"),
                    Triple("15.6 km", "35 min", "₹ 234"),
                    Triple("9.2 km", "22 min", "₹ 138")
                )
                itemsIndexed(trips) { _, trip ->
                    TripItem(
                        distance = trip.first,
                        duration = trip.second,
                        amount = trip.third
                    )
                }
            }
        }
    }
}

@Composable
fun TripItem(distance: String, duration: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCard.copy(alpha = 0.5f))
            .border(1.dp, SurfaceElevated, RoundedCornerShape(8.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = distance, style = MaterialTheme.typography.titleLarge, color = TextHighContrast, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = duration, style = MaterialTheme.typography.bodyLarge, color = TextMuted, fontSize = 13.sp)
        }
        Text(text = amount, style = MaterialTheme.typography.titleLarge, color = SafetyGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
