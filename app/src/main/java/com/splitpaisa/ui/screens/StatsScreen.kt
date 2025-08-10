package com.splitpaisa.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun StatsScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card { Column(Modifier.padding(16.dp)) { Text("Monthly Trend", style = MaterialTheme.typography.titleLarge); LineDemo() } }
        Card { Column(Modifier.padding(16.dp)) { Text("Cash-flow (weekly)", style = MaterialTheme.typography.titleLarge); BarsDemo() } }
        Card { Column(Modifier.padding(16.dp)) { Text("Daily heatmap — Aug 2025", style = MaterialTheme.typography.titleLarge); HeatDemo() } }
    }
}

@Composable
private fun LineDemo() {
    Canvas(Modifier.fillMaxWidth().height(120.dp).padding(8.dp)) {
        val step = size.width / 6f
        val base = size.height * 0.8f
        val points = listOf(10f, 40f, 20f, 70f, 50f, 90f, 110f)
        for (i in 1 until points.size) {
            drawLine(
                color = MaterialTheme.colorScheme.primary,
                start = Offset(step * (i-1), base - points[i-1]),
                end = Offset(step * i, base - points[i]),
                strokeWidth = 6f, cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun BarsDemo() {
    Canvas(Modifier.fillMaxWidth().height(120.dp).padding(8.dp)) {
        val barWidth = size.width / 10f
        val gap = barWidth * 0.6f
        val values = listOf(80f, 35f, 70f, 60f)
        values.forEachIndexed { i, v ->
            drawRoundRect(
                color = MaterialTheme.colorScheme.primary,
                topLeft = Offset((barWidth + gap) * i, size.height - v),
                size = androidx.compose.ui.geometry.Size(barWidth, v),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
            )
        }
    }
}

@Composable
private fun HeatDemo() {
    Canvas(Modifier.fillMaxWidth().height(100.dp).padding(8.dp)) {
        val cols = 8
        val rows = 3
        val cell = size.width / (cols * 1.25f)
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val x = c * (cell * 1.25f)
                val y = r * (cell * 1.25f)
                drawRoundRect(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = (0.3f + (r*cols+c) % 5 / 10f)),
                    topLeft = Offset(x, y),
                    size = androidx.compose.ui.geometry.Size(cell, cell),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cell/4, cell/4)
                )
            }
        }
    }
}
