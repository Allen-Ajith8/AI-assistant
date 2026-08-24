package com.example.gigshield.ui.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Color

// AGSL Shader for the wavy background
const val WAVY_SHADER = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 horizonColor;
    layout(color) uniform half4 waveColor;

    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord.xy / resolution.xy;
        
        // Create an undulating wave effect
        float wave = sin(uv.x * 4.0 + time * 0.8) * 0.1;
        wave += cos(uv.x * 6.0 - time * 0.4) * 0.05;
        
        // Map the colors (Deep Purple to Magenta)
        float mixValue = clamp(uv.y + wave, 0.0, 1.0);
        return mix(horizonColor, waveColor, mixValue);
    }
"""

@Composable
fun GradientWaveBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveTransition")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveTime"
    )

    Box(modifier = modifier
        .fillMaxSize()
        .drawWithCache {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val shader = RuntimeShader(WAVY_SHADER)
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setFloatUniform("time", time)
                shader.setColorUniform("horizonColor", android.graphics.Color.parseColor("#0A0A0A")) // Deep Black
                shader.setColorUniform("waveColor", android.graphics.Color.parseColor("#FFB800"))    // iQOO Yellow

                onDrawBehind {
                    drawRect(brush = ShaderBrush(shader))
                }
            } else {
                // Fallback for older Android versions (static gradient)
                onDrawBehind { drawRect(Color(0xFF332500)) }
            }
        }
    ) {
        content()
    }
}
