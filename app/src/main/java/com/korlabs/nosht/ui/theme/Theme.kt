package com.korlabs.nosht.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1B5E20),           // Forest Green
    onPrimary = Color.White,               // Text on primary buttons
    secondary = Color(0xFFFF8F00),         // Softer Amber
    onSecondary = Color.Black,             // Text on secondary buttons
    background = Color(0xFF121212),        // Dark Gray for background
    onBackground = Color(0xFFE0E0E0),      // Light Gray for primary text
    surface = Color(0xFF1E1E1E),           // Medium-dark Gray for cards and surfaces
    onSurface = Color(0xFFB0BEC5),         // Gray for secondary text
    surfaceVariant = Color(0xFF33691E),    // Olive Dark Green for surface highlights
    onSurfaceVariant = Color(0xFF5A925D),  // Softer green for contrast
    outlineVariant = Color(0xFF757575)     // Medium Gray outline for borders or dividers
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),           // Deep Green
    onPrimary = Color.White,               // Text on primary buttons
    secondary = Color(0xFFFB8C00),         // Toasted Orange
    onSecondary = Color.White,             // Text on secondary buttons
    background = Color(0xFFFAFAFA),        // Neutral White for background
    onBackground = Color(0xFF212121),      // Dark Gray for primary text
    surface = Color(0xFFF5F5F5),           // Light Gray for cards and surfaces
    onSurface = Color(0xFF757575),         // Medium Gray for secondary text
    surfaceVariant = Color(0xFFDCEDC8),    // Softer sage green for surface highlights
    onSurfaceVariant = Color(0xFF4CAF50),  // Less saturated green for contrast
    outlineVariant = Color(0xFFBDBDBD)     // Gray outline for borders or dividers
)

@Composable
fun NoshtTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme,
        typography = CustomTypography,
        content = content
    )
}