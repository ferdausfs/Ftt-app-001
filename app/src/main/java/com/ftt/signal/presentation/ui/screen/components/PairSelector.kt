package com.ftt.signal.presentation.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftt.signal.presentation.ui.theme.BorderColor
import com.ftt.signal.presentation.ui.theme.CardBg
import com.ftt.signal.presentation.ui.theme.Primary
import com.ftt.signal.presentation.ui.theme.Surface
import com.ftt.signal.presentation.ui.theme.TextPrimary
import com.ftt.signal.presentation.ui.theme.TextSecondary

object PairCatalog {
    val forexMajor = listOf(
        "EUR/USD", "GBP/USD", "USD/JPY", "USD/CHF", "USD/CAD",
        "AUD/USD", "NZD/USD", "EUR/GBP", "EUR/JPY", "GBP/JPY",
        "AUD/JPY", "EUR/AUD", "EUR/CHF", "EUR/CAD", "GBP/CHF",
        "GBP/AUD", "AUD/CAD", "AUD/CHF", "NZD/JPY", "CAD/JPY",
        "CHF/JPY"
    )
    val forexOtc = forexMajor.map { "$it-OTC" }
    val crypto = listOf("BTC/USD", "ETH/USD", "BNB/USD", "XRP/USD", "SOL/USD", "ADA/USD", "DOGE/USD")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairSelector(
    selected: String,
    onSelect: (String) -> Unit,
    onGetSignal: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    SectionCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Trading Pair",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selected,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedTrailingIconColor = TextPrimary,
                        unfocusedTrailingIconColor = TextSecondary
                    ),
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth()
                )

                androidx.compose.material3.ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(CardBg)
                        .heightIn(max = 380.dp)
                ) {
                    Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Column {
                            GroupHeader("FOREX MAJOR")
                            PairCatalog.forexMajor.forEach { pair ->
                                DropdownItem(pair) {
                                    onSelect(pair)
                                    expanded = false
                                }
                            }
                            HorizontalDivider(color = BorderColor)
                            GroupHeader("FOREX OTC")
                            PairCatalog.forexOtc.forEach { pair ->
                                DropdownItem(pair) {
                                    onSelect(pair)
                                    expanded = false
                                }
                            }
                            HorizontalDivider(color = BorderColor)
                            GroupHeader("CRYPTO")
                            PairCatalog.crypto.forEach { pair ->
                                DropdownItem(pair) {
                                    onSelect(pair)
                                    expanded = false
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onGetSignal,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.Black,
                    disabledContainerColor = Primary.copy(alpha = 0.5f),
                    disabledContentColor = Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = Color.Black,
                        modifier = Modifier.height(20.dp)
                    )
                } else {
                    Text(
                        "GET SIGNAL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupHeader(label: String) {
    Text(
        text = label,
        color = Primary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun DropdownItem(label: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(label, color = TextPrimary) },
        onClick = onClick
    )
}
