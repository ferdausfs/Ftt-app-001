package com.ftt.signal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ftt.signal.presentation.ui.screen.components.BatchCard
import com.ftt.signal.presentation.ui.screen.components.ErrorCard
import com.ftt.signal.presentation.ui.screen.components.HealthCard
import com.ftt.signal.presentation.ui.screen.components.HistoryCard
import com.ftt.signal.presentation.ui.screen.components.PairSelector
import com.ftt.signal.presentation.ui.screen.components.ReportButtons
import com.ftt.signal.presentation.ui.screen.components.SignalCard
import com.ftt.signal.presentation.ui.screen.components.StatsCard
import com.ftt.signal.presentation.ui.theme.Background
import com.ftt.signal.presentation.ui.theme.CallColor
import com.ftt.signal.presentation.ui.theme.PutColor
import com.ftt.signal.presentation.ui.theme.Surface
import com.ftt.signal.presentation.ui.theme.TextPrimary
import com.ftt.signal.presentation.ui.theme.TextSecondary
import com.ftt.signal.presentation.viewmodel.SignalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: SignalViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.reportStatus) {
        state.reportStatus?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearReportStatus()
        }
    }

    val healthOk = state.health?.status?.equals("healthy", true) == true ||
        state.health?.status?.equals("ok", true) == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "FTT Signal",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "v6.9.2 · Cloudflare Worker",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    color = when {
                                        state.health == null -> TextSecondary
                                        healthOk -> CallColor
                                        else -> PutColor
                                    },
                                    shape = CircleShape
                                )
                        )
                        Spacer(Modifier.size(4.dp))
                        IconButton(
                            onClick = { viewModel.checkHealth() },
                            enabled = !state.isHealthLoading
                        ) {
                            if (state.isHealthLoading) {
                                CircularProgressIndicator(
                                    color = TextPrimary,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Filled.Refresh,
                                    contentDescription = "Health check",
                                    tint = TextPrimary
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    titleContentColor = TextPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(PaddingValues(horizontal = 12.dp, vertical = 12.dp)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Error
            state.error?.let { err ->
                ErrorCard(message = err, onDismiss = { viewModel.clearError() })
            }

            // Pair selector
            PairSelector(
                selected = state.selectedPair,
                onSelect = { viewModel.selectPair(it) },
                onGetSignal = { viewModel.fetchSignal() },
                isLoading = state.isLoading
            )

            // Signal result
            state.signal?.let { sig ->
                SignalCard(signal = sig)
                if (sig.signal?.id != null) {
                    ReportButtons(
                        onWin = { viewModel.reportResult("WIN") },
                        onLoss = { viewModel.reportResult("LOSS") },
                        isReporting = state.isReporting
                    )
                }
            }

            // Stats
            StatsCard(
                expanded = state.statsExpanded,
                stats = state.stats,
                isLoading = state.isStatsLoading,
                onToggle = { viewModel.toggleStats() },
                onRefresh = { viewModel.loadStats() }
            )

            // History
            HistoryCard(
                expanded = state.historyExpanded,
                history = state.history,
                isLoading = state.isHistoryLoading,
                onToggle = { viewModel.toggleHistory() }
            )

            // Batch
            BatchCard(
                expanded = state.batchExpanded,
                onToggle = { viewModel.toggleBatch() },
                pair1 = state.batchPair1,
                pair2 = state.batchPair2,
                pair3 = state.batchPair3,
                onPairChange = { i, p -> viewModel.setBatchPair(i, p) },
                onFetch = { viewModel.fetchBatch() },
                isLoading = state.isBatchLoading,
                result = state.batchResult
            )

            // Health
            HealthCard(
                expanded = state.healthExpanded,
                health = state.health,
                onToggle = { viewModel.toggleHealth() }
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
