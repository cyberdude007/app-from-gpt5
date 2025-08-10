package com.splitpaisa.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun StatsScreen() {
    // Hoist MaterialTheme colors in a composable context and pass to drawing functions.
    val primary = MaterialTheme.colorScheme.primary

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Monthly Trend", style = MaterialTheme.typography.titleLarge)
                LineDemo(primary)
            }
        }
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Cash-flow (weekly)", style = MaterialTheme.typography.titleLarge)
                BarsDemo(primary)
            }
        }
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Daily heatmap — Aug 2025", style = MaterialTheme.typography.titleLarge)
                HeatDemo(primary)
            }
        }
    }
}

@Composable
private fun LineDemo(primary: Color) {
    Canvas(Modifier.fillMaxWidth().height(120.dp).padding(8.dp)) {
        val step = size.width / 6f
        val base = size.height * 0.8f
        val points = listOf(10f, 40f, 20f, 70f, 50f, 90f, 110f)
        for (i in 1 until points.size) {
            drawLine(
                color = primary,
                start = Offset(step * (i-1), base - points[i-1]),
                end = Offset(step * i, base - points[i]),
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun BarsDemo(primary: Color) {
    Canvas(Modifier.fillMaxWidth().height(120.dp).padding(8.dp)) {
        val barWidth = size.width / 10f
        val gap = barWidth * 0.6f
        val values = listOf(80f, 35f, 70f, 60f)
        values.forEachIndexed { i, v ->
            drawRoundRect(
                color = primary,
                topLeft = Offset((barWidth + gap) * i, size.height - v),
                size = Size(barWidth, v),
                cornerRadius = CornerRadius(12f, 12f)
            )
        }
    }
}

@Composable
private fun HeatDemo(primary: Color) {
    Canvas(Modifier.fillMaxWidth().height(100.dp).padding(8.dp)) {
        val cols = 8
        val rows = 3
        val cell = size.width / (cols * 1.25f)
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val x = c * (cell * 1.25f)
                val y = r * (cell * 1.25f)
                val alpha = 0.3f + ((r * cols + c) % 5) / 10f
                drawRoundRect(
                    color = primary.copy(alpha = alpha),
                    topLeft = Offset(x, y),
                    size = Size(cell, cell),
                    cornerRadius = CornerRadius(cell / 4, cell / 4)
                )
            }
        }
    }
}
