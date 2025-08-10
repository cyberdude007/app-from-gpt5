package com.splitpaisa.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun SimpleLineChart(values: List<Float>, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
    Canvas(modifier = modifier) {
        if (values.isEmpty()) return@Canvas
        val w = size.width
        val h = size.height
        val maxV = values.maxOrNull() ?: 1f
        val minV = values.minOrNull() ?: 0f
        val span = (maxV - minV).let { if (it == 0f) 1f else it }
        val stepX = w / (values.size - 1)
        val path = Path()
        values.forEachIndexed { i, v ->
            val x = stepX * i
            val y = h - ((v - minV) / span) * h
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
    }
}

@Composable
fun SimpleBarChart(pairs: List<Pair<String, Float>>, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
    Canvas(modifier = modifier) {
        if (pairs.isEmpty()) return@Canvas
        val w = size.width
        val h = size.height
        val barWidth = w / (pairs.size * 1.5f)
        val maxV = pairs.maxOf { it.second }
        pairs.forEachIndexed { i, (_, v) ->
            val x = (i * 1.5f + 0.25f) * barWidth
            val bh = (v / maxV) * h
            drawRect(color = color, topLeft = Offset(x, h - bh), size = androidx.compose.ui.geometry.Size(barWidth, bh))
        }
    }
}
