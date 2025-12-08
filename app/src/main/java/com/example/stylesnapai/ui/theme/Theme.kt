package com.example.stylesnapai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonPink,
    tertiary = PurpleGrey40,
    background = DeepBlue,
    surface = DeepBlue
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = NeonPink,
    tertiary = PurpleGrey40,
    background = ColorWhite,
    surface = ColorWhite
)

private val ColorWhite = androidx.compose.ui.graphics.Color(0xFFFFFFFF)

@Composable
fun StyleSnapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalContext.current as Activity
    val window = view.window
    window.statusBarColor = colorScheme.primary.toArgb()
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !darkTheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
