package com.ftt.signal.presentation.ui.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftt.signal.data.model.BatchResponse
import com.ftt.signal.data.model.SignalResponse
import com.ftt.signal.presentation.ui.theme.BorderColor
import com.ftt.signal.presentation.ui.theme.CardBg
import com.ftt.signal.presentation.ui.theme.Primary
import com.ftt.signal.presentation.ui.theme.Surface
import com.ftt.signal.presentation.ui.theme.TextPrimary
import com.ftt.signal.presentation.ui.theme.TextSecondary

@Composable
fun BatchCard(
    expanded: Boolean,
    onToggle: () -> Unit,
    pair1: String,
    pair2: String,
    pair3: String,
    onPairChange: (Int, String) -> Unit,
    onFetch: () -> Unit,
    isLoading: Boolean,
    result: BatchResponse?,
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
                Text("⚡ Batch Signal", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Text(if (expanded) "▲" else "▼", color = TextSecondary)
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    HorizontalDivider(color = BorderColor)
                    Spacer(Modifier.height(12.dp))

                    Text("Up to 3 pairs (comma-free)", color = TextSecondary, fontSize = 11.sp)
                    Spacer(Modifier.height(8.dp))

                    PairField("Pair 1", pair1) { onPairChange(0, it) }
                    Spacer(Modifier.height(8.dp))
                    PairField("Pair 2", pair2) { onPairChange(1, it) }
                    Spacer(Modifier.height(8.dp))
                    PairField("Pair 3", pair3) { onPairChange(2, it) }

                    Spacer(Modifier.height(14.dp))

                    Button(
                        onClick = onFetch,
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = Color.Black
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = Color.Black,
                                modifier = Modifier.height(18.dp)
                            )
                        } else {
                            Text("GET BATCH SIGNAL", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    result?.results?.let { list ->
                        list.forEach { sig ->
                            CompactSignalRow(sig)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun PairField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, color = TextSecondary) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = BorderColor,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = Surface,
            unfocusedContainerColor = Surface,
            focusedLabelColor = Primary,
            unfocusedLabelColor = TextSecondary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CompactSignalRow(sig: SignalResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg, RoundedCornerShape(6.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(end = 8.dp)) {
            Text(sig.pair ?: "—", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(
                "${sig.signal?.timeframe ?: ""} · ${sig.session?.name ?: ""}",
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${sig.signal?.confidence ?: 0}%",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            DirectionBadge(direction = sig.signal?.direction)
        }
    }
}
