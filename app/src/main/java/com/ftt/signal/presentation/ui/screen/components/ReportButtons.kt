package com.ftt.signal.presentation.ui.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ftt.signal.presentation.ui.theme.CallColor
import com.ftt.signal.presentation.ui.theme.PutColor

@Composable
fun ReportButtons(
    onWin: () -> Unit,
    onLoss: () -> Unit,
    isReporting: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onWin,
            enabled = !isReporting,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            border = BorderStroke(1.dp, CallColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CallColor)
        ) {
            if (isReporting) {
                CircularProgressIndicator(strokeWidth = 2.dp, color = CallColor, modifier = Modifier.height(18.dp))
            } else {
                Text("✅ WIN", fontWeight = FontWeight.Bold)
            }
        }

        OutlinedButton(
            onClick = onLoss,
            enabled = !isReporting,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            border = BorderStroke(1.dp, PutColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PutColor)
        ) {
            if (isReporting) {
                CircularProgressIndicator(strokeWidth = 2.dp, color = PutColor, modifier = Modifier.height(18.dp))
            } else {
                Text("❌ LOSS", fontWeight = FontWeight.Bold)
            }
        }
    }
}
