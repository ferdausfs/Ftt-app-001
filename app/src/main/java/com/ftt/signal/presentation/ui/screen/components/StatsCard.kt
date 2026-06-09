package com.ftt.signal.presentation.ui.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.ftt.signal.data.model.StatsResponse
import com.ftt.signal.presentation.ui.theme.BorderColor
import com.ftt.signal.presentation.ui.theme.CallColor
import com.ftt.signal.presentation.ui.theme.Primary
import com.ftt.signal.presentation.ui.theme.PutColor
import com.ftt.signal.presentation.ui.theme.TextPrimary
import com.ftt.signal.presentation.ui.theme.TextSecondary

@Composable
fun StatsCard(
    expanded: Boolean,
    stats: StatsResponse?,
    isLoading: Boolean,
    onToggle: () -> Unit,
    onRefresh: () -> Unit,
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
                Text(
                    "📊 Pair Statistics",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(if (expanded) "▲" else "▼", color = TextSecondary)
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    HorizontalDivider(color = BorderColor)
                    Spacer(Modifier.height(12.dp))

                    if (isLoading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Primary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.height(28.dp)
                            )
                        }
                    } else if (stats?.stats != null) {
                        val s = stats.stats
                        val winRate = s.winRate ?: 0.0
                        Text(
                            "${"%.1f".format(winRate)}%",
                            color = if (winRate >= 50) CallColor else PutColor,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Win Rate", color = TextSecondary, fontSize = 12.sp)
                        Spacer(Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatItem("Total", "${s.totalSignals ?: 0}", Modifier.weight(1f))
                            StatItem("Wins", "${s.wins ?: 0}", Modifier.weight(1f), CallColor)
                            StatItem("Losses", "${s.losses ?: 0}", Modifier.weight(1f), PutColor)
                        }
                        s.lastUpdated?.let {
                            Spacer(Modifier.height(8.dp))
                            Text("Last updated: $it", color = TextSecondary, fontSize = 10.sp)
                        }
                    } else {
                        Text("No statistics yet — tap refresh.", color = TextSecondary, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = onRefresh,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("REFRESH STATS", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = TextPrimary
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = TextSecondary, fontSize = 11.sp)
    }
}
