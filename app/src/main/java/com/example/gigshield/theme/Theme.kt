package com.example.gigshield.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KineticShieldColorScheme = darkColorScheme(
    primary = SafetyGreen,
    secondary = ElectricBlue,
    tertiary = SurfaceElevated,
    background = DeepBase,
    surface = SurfaceCard,
    surfaceVariant = SurfaceElevated,
    onPrimary = DeepBase,
    onSecondary = DeepBase,
    onBackground = TextHighContrast,
    onSurface = TextHighContrast,
    onSurfaceVariant = TextMuted,
    error = ErrorRed
)

@Composable
fun GigShieldTheme(
    content: @Composable () -> Unit
) {
    // Enforce Dark Mode First for the Kinetic Shield design
    val colorScheme = KineticShieldColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
