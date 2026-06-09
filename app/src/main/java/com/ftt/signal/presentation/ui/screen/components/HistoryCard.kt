package com.ftt.signal.presentation.ui.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftt.signal.data.model.HistorySignal
import com.ftt.signal.data.model.HistoryResponse
import com.ftt.signal.presentation.ui.theme.BorderColor
import com.ftt.signal.presentation.ui.theme.CallColor
import com.ftt.signal.presentation.ui.theme.NeutralColor
import com.ftt.signal.presentation.ui.theme.Primary
import com.ftt.signal.presentation.ui.theme.PutColor
import com.ftt.signal.presentation.ui.theme.TextPrimary
import com.ftt.signal.presentation.ui.theme.TextSecondary

@Composable
fun HistoryCard(
    expanded: Boolean,
    history: HistoryResponse?,
    isLoading: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("📋 Signal History", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Text(if (expanded) "▲" else "▼", color = TextSecondary)
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                    HorizontalDivider(color = BorderColor)
                    Spacer(Modifier.height(8.dp))

                    when {
                        isLoading -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Primary,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }
                        history?.signals.isNullOrEmpty() -> {
                            Text(
                                "No history available.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 360.dp)
                            ) {
                                items(history?.signals.orEmpty()) { item ->
                                    HistoryRow(item)
                                    HorizontalDivider(color = BorderColor.copy(alpha = 0.4f))
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(item: HistorySignal) {
    val dirColor = when (item.direction?.uppercase()) {
        "CALL" -> CallColor
        "PUT" -> PutColor
        else -> NeutralColor
    }
    val resultColor = when (item.result?.uppercase()) {
        "WIN" -> CallColor
        "LOSS" -> PutColor
        else -> NeutralColor
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ChipPill(item.direction ?: "—", dirColor)
            Spacer(Modifier.padding(4.dp))
            Text("${item.confidence ?: 0}%", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            ChipPill(item.result ?: "PENDING", resultColor)
            Spacer(Modifier.padding(4.dp))
            Text(
                shortTimestamp(item.timestamp),
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun ChipPill(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(label.uppercase(), color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

private fun shortTimestamp(ts: String?): String {
    if (ts.isNullOrBlank()) return "—"
    // Take HH:MM if ISO, otherwise raw
    return try {
        val timeIdx = ts.indexOf('T')
        if (timeIdx >= 0 && ts.length >= timeIdx + 6) ts.substring(timeIdx + 1, timeIdx + 6)
        else ts.take(16)
    } catch (_: Exception) {
        ts
    }
}
