package com.splitpaisa.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val measurables = subcompose("content", content)
        val placeables = measurables.map { it.measure(constraints.copy(minWidth=0, minHeight=0)) }
        val rows = mutableListOf<List<Int>>()
        var current = mutableListOf<Int>()
        var rowWidth = 0
        val maxWidth = constraints.maxWidth
        placeables.forEachIndexed { i, p ->
            val itemW = p.width + when(horizontalArrangement){ is Arrangement.spacedBy -> horizontalArrangement.spacing.roundToPx(); else -> 0 }
            if (rowWidth + itemW > maxWidth && current.isNotEmpty()) { rows.add(current); current = mutableListOf(i); rowWidth = itemW }
            else { current.add(i); rowWidth += itemW }
        }
        if (current.isNotEmpty()) rows.add(current)
        val rowHeights = rows.map { r -> r.maxOf { placeables[it].height } }
        val height = rowHeights.sum() + when(verticalArrangement){ is Arrangement.spacedBy -> verticalArrangement.spacing.roundToPx() * (rows.size-1); else -> 0 }
        val width = maxWidth
        layout(width, height) {
            var y = 0
            rows.forEachIndexed { idx, r ->
                var x = 0
                r.forEach { id ->
                    val p = placeables[id]
                    p.place(x, y)
                    x += p.width + when(horizontalArrangement){ is Arrangement.spacedBy -> horizontalArrangement.spacing.roundToPx(); else -> 0 }
                }
                y += rowHeights[idx] + when(verticalArrangement){ is Arrangement.spacedBy -> verticalArrangement.spacing.roundToPx(); else -> 0 }
            }
        }
    }
}
