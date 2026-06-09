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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftt.signal.data.model.SignalResponse
import com.ftt.signal.presentation.ui.theme.CallColor
import com.ftt.signal.presentation.ui.theme.ConfHigh
import com.ftt.signal.presentation.ui.theme.ConfLow
import com.ftt.signal.presentation.ui.theme.ConfMid
import com.ftt.signal.presentation.ui.theme.MonoStyle
import com.ftt.signal.presentation.ui.theme.NeutralColor
import com.ftt.signal.presentation.ui.theme.PutColor
import com.ftt.signal.presentation.ui.theme.Surface
import com.ftt.signal.presentation.ui.theme.TextPrimary
import com.ftt.signal.presentation.ui.theme.TextSecondary
import com.ftt.signal.presentation.ui.theme.WarningColor

@Composable
fun SignalCard(signal: SignalResponse, modifier: Modifier = Modifier) {
    val data = signal.signal
    val marketClosed = signal.marketStatus.equals("CLOSED", ignoreCase = true)

    SectionCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {

            if (marketClosed) {
                MarketClosedBanner(signal.nextOpen ?: signal.message)
                Spacer(Modifier.height(12.dp))
            }

            // Header row: pair + direction
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        signal.pair ?: "—",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    signal.assetType?.let {
                        Text(it, color = TextSecondary, fontSize = 11.sp)
                    }
                }
                DirectionBadge(direction = data?.direction)
            }

            Spacer(Modifier.height(16.dp))

            // Confidence
            val confidence = data?.confidence ?: 0
            val confColor = when {
                confidence > 75 -> ConfHigh
                confidence in 60..75 -> ConfMid
                else -> ConfLow
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Confidence", color = TextSecondary, fontSize = 12.sp)
                Text(
                    "$confidence%",
                    color = confColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (confidence.coerceIn(0, 100)) / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = confColor,
                trackColor = Surface
            )

            Spacer(Modifier.height(14.dp))

            // Duration
            data?.duration?.let {
                Text(
                    "Hold for $it min · ${data.timeframe ?: ""}",
                    color = TextPrimary,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(12.dp))
            }

            // Entry / SL / TP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PriceCell("Entry", data?.entryPrice, Modifier.weight(1f), TextPrimary)
                PriceCell("SL", data?.sl, Modifier.weight(1f), PutColor)
                PriceCell("TP", data?.tp, Modifier.weight(1f), CallColor)
            }

            Spacer(Modifier.height(12.dp))

            // Session
            signal.session?.let { s ->
                Row {
                    Text("Session: ", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        "${s.name ?: "—"} · ${s.quality ?: "—"} QUALITY",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            // Reasoning (expandable)
            data?.reasoning?.takeIf { it.isNotBlank() }?.let { reasoning ->
                var expanded by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Reasoning",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        if (expanded) "▲ collapse" else "▼ expand",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
                AnimatedVisibility(visible = expanded) {
                    Text(
                        reasoning,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                if (!expanded) {
                    Text(
                        reasoning,
                        color = TextPrimary.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))
            }

            // Signal ID
            data?.id?.let {
                Text("ID: $it", color = TextSecondary, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun DirectionBadge(direction: String?) {
    val (color, label) = when (direction?.uppercase()) {
        "CALL" -> CallColor to "CALL ▲"
        "PUT" -> PutColor to "PUT ▼"
        else -> NeutralColor to "NEUTRAL ●"
    }
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            label,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun PriceCell(
    label: String,
    value: Double?,
    modifier: Modifier = Modifier,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .background(Surface, RoundedCornerShape(6.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(2.dp))
        Text(
            text = value?.let { formatPrice(it) } ?: "—",
            color = valueColor,
            style = MonoStyle
        )
    }
}

@Composable
fun MarketClosedBanner(detail: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(WarningColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                "⚠ Forex Market Closed",
                color = WarningColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            if (!detail.isNullOrBlank()) {
                Text(detail, color = TextPrimary, fontSize = 12.sp)
            }
        }
    }
}

private fun formatPrice(v: Double): String {
    return if (v >= 1000) String.format("%.2f", v)
    else if (v >= 1) String.format("%.4f", v)
    else String.format("%.5f", v)
}

@Composable
fun ErrorCard(message: String, onDismiss: () -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF2A1418)),
        border = androidx.compose.foundation.BorderStroke(1.dp, PutColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(end = 8.dp)) {
                Text("Error", color = PutColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(message, color = TextPrimary, fontSize = 12.sp)
            }
            Text(
                "✕",
                color = TextSecondary,
                fontSize = 18.sp,
                modifier = Modifier
                    .clickable(onClick = onDismiss)
                    .padding(6.dp)
            )
        }
    }
}


