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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftt.signal.data.model.HealthResponse
import com.ftt.signal.presentation.ui.theme.BorderColor
import com.ftt.signal.presentation.ui.theme.CallColor
import com.ftt.signal.presentation.ui.theme.PutColor
import com.ftt.signal.presentation.ui.theme.TextPrimary
import com.ftt.signal.presentation.ui.theme.TextSecondary
import com.ftt.signal.presentation.ui.theme.WarningColor

@Composable
fun HealthCard(
    expanded: Boolean,
    health: HealthResponse?,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHealthy = health?.status?.equals("healthy", true) == true ||
        health?.status?.equals("ok", true) == true

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (health == null) TextSecondary
                                else if (isHealthy) CallColor else PutColor,
                                CircleShape
                            )
                    )
                    Spacer(Modifier.padding(4.dp))
                    Text("💚 Worker Health", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
                Text(if (expanded) "▲" else "▼", color = TextSecondary)
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    HorizontalDivider(color = BorderColor)
                    Spacer(Modifier.height(12.dp))

                    if (health == null) {
                        Text("Tap refresh icon at top to check.", color = TextSecondary, fontSize = 12.sp)
                    } else {
                        HealthRow("Status", health.status ?: "unknown",
                            if (isHealthy) CallColor else PutColor)
                        health.version?.let { HealthRow("Version", it) }
                        health.apiKeysConfigured?.let { HealthRow("API Keys", "$it configured") }
                        health.kvCache?.let { HealthRow("KV Cache", it) }
                        health.currentSession?.let { HealthRow("Session", it) }
                        health.markets?.forex?.let {
                            HealthRow(
                                "Forex",
                                it,
                                if (it.equals("OPEN", true)) CallColor else WarningColor
                            )
                        }
                        health.markets?.crypto?.let {
                            HealthRow("Crypto", it, CallColor)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun HealthRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextSecondary, fontSize = 12.sp)
        Text(value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
