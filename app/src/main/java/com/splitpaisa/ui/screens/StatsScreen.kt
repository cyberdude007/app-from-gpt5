package com.splitpaisa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.splitpaisa.ui.components.SimpleBarChart
import com.splitpaisa.ui.components.SimpleLineChart
import java.time.LocalDate
import kotlin.random.Random

@Composable
fun StatsScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Monthly Trend", style = MaterialTheme.typography.titleLarge)
                SimpleLineChart(values = listOf(8f,9f,7.6f,10.4f,9.8f,11.25f), modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp))
            }
        }
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Cash-flow (weekly)", style = MaterialTheme.typography.titleLarge)
                SimpleBarChart(
                    pairs = listOf("W1" to 4.5f, "W2" to 2f, "W3" to 3.2f, "W4" to 2.5f),
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                )
            }
        }
        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Daily heatmap — Aug ${LocalDate.now().year}", style = MaterialTheme.typography.titleLarge)
                HeatmapMonth()
            }
        }
    }
}

@Composable
private fun HeatmapMonth() {
    val cols = 7
    val rows = 6
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(cols) {
                    val level = kotlin.random.Random.nextInt(0,5)
                    val color = when (level) {
                        0 -> Color(0xFFE6F7F2)
                        1 -> Color(0xFFC7EFE4)
                        2 -> Color(0xFF9EE3D2)
                        3 -> Color(0xFF6DD1BA)
                        else -> Color(0xFF2BB39A)
                    }
                    Box(Modifier.size(22.dp).background(color, shape = MaterialTheme.shapes.small))
                }
            }
        }
    }
}
