package com.splitpaisa.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Teal = Color(0xFF2BB39A)
val Lime = Color(0xFFB7E041)
val Gold = Color(0xFFC89B3C)

private val Light = lightColorScheme(primary = Teal, secondary = Lime, tertiary = Gold)
private val Dark = darkColorScheme(primary = Teal, secondary = Lime, tertiary = Gold)

@Composable
fun SplitPaisaTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(colorScheme = if (dark) Dark else Light, content = content)
}
