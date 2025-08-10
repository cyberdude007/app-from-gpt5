package com.splitpaisa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

val PFTeal = Color(0xFF2BB39A)
val PFLime = Color(0xFFB7E041)
val PFGold = Color(0xFFC89B3C)

private val LightColors = lightColorScheme(
    primary = PFTeal,
    secondary = PFLime,
    tertiary = PFGold
)

private val DarkColors = darkColorScheme(
    primary = PFTeal,
    secondary = PFLime,
    tertiary = PFGold
)

private val SplitShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun SplitPaisaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        shapes = SplitShapes,
        typography = Typography(),
        content = content
    )
}
