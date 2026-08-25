package com.example.gigshield.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigshield.theme.DeepBase
import com.example.gigshield.theme.ElectricBlue
import com.example.gigshield.theme.SafetyGreen
import com.example.gigshield.theme.SurfaceCard
import com.example.gigshield.theme.SurfaceElevated
import com.example.gigshield.theme.TextHighContrast
import com.example.gigshield.theme.TextMuted

@Composable
fun WeeklyEarningsChart(
    dataPoints: List<Float>,
    daysOfWeek: List<String>,
    modifier: Modifier = Modifier,
    lineColor: Color = SafetyGreen,
    fillColor: Color = SafetyGreen.copy(alpha = 0.2f)
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    
    // Animation for line drawing
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(dataPoints) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, tween(1000, easing = FastOutSlowInEasing))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard.copy(alpha = 0.7f))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weekly Earnings",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextHighContrast,
                    fontWeight = FontWeight.Bold
                )
                if (selectedIndex != null) {
                    Text(
                        text = "₹${dataPoints[selectedIndex!!].toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = lineColor,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Total: ₹${dataPoints.sum().toInt()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                // Calculate which point was tapped
                                val width = size.width
                                val stepX = width / (dataPoints.size - 1)
                                val index = (offset.x / stepX).toInt()
                                val remainder = offset.x % stepX
                                selectedIndex = if (remainder > stepX / 2 && index < dataPoints.size - 1) {
                                    index + 1
                                } else {
                                    index
                                }.coerceIn(0, dataPoints.size - 1)
                            }
                        }
                ) {
                    val maxValue = (dataPoints.maxOrNull() ?: 1f).coerceAtLeast(1f)
                    val width = size.width
                    val height = size.height
                    val stepX = width / (dataPoints.size - 1)

                    // Draw grid lines
                    val gridLines = 4
                    for (i in 0 until gridLines) {
                        val y = height - (height * (i / (gridLines - 1).toFloat()))
                        drawLine(
                            color = SurfaceElevated.copy(alpha = 0.5f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Create path for the line
                    val path = Path()
                    val fillPath = Path()
                    
                    val points = mutableListOf<Offset>()

                    dataPoints.forEachIndexed { index, value ->
                        val x = index * stepX
                        val y = height - (value / maxValue * height)
                        points.add(Offset(x, y))
                        
                        if (index == 0) {
                            path.moveTo(x, y)
                            fillPath.moveTo(x, height)
                            fillPath.lineTo(x, y)
                        } else {
                            // Smooth bezier curve
                            val prevX = (index - 1) * stepX
                            val prevY = height - (dataPoints[index - 1] / maxValue * height)
                            
                            val controlPoint1 = Offset(prevX + stepX / 2, prevY)
                            val controlPoint2 = Offset(x - stepX / 2, y)
                            
                            path.cubicTo(
                                controlPoint1.x, controlPoint1.y,
                                controlPoint2.x, controlPoint2.y,
                                x, y
                            )
                            fillPath.cubicTo(
                                controlPoint1.x, controlPoint1.y,
                                controlPoint2.x, controlPoint2.y,
                                x, y
                            )
                        }
                    }

                    // Draw fill
                    fillPath.lineTo(width, height)
                    fillPath.close()
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(fillColor, Color.Transparent),
                            startY = 0f,
                            endY = height
                        ),
                        alpha = animationProgress.value
                    )

                    // Draw line
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                        alpha = animationProgress.value
                    )
                    
                    // Draw interactive dots
                    if (animationProgress.value == 1f) {
                        points.forEachIndexed { index, offset ->
                            val isSelected = index == selectedIndex
                            val radius = if (isSelected) 6.dp.toPx() else 3.dp.toPx()
                            
                            drawCircle(
                                color = DeepBase,
                                radius = radius,
                                center = offset
                            )
                            drawCircle(
                                color = if (isSelected) Color.White else lineColor,
                                radius = radius,
                                center = offset,
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // X-axis labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfWeek.forEachIndexed { index, day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (index == selectedIndex) TextHighContrast else TextMuted,
                        fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
