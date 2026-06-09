package com.ftt.signal.presentation.viewmodel

import com.ftt.signal.data.model.BatchResponse
import com.ftt.signal.data.model.HealthResponse
import com.ftt.signal.data.model.HistoryResponse
import com.ftt.signal.data.model.SignalResponse
import com.ftt.signal.data.model.StatsResponse

data class SignalUiState(
    val isLoading: Boolean = false,
    val isHealthLoading: Boolean = false,
    val isStatsLoading: Boolean = false,
    val isHistoryLoading: Boolean = false,
    val isBatchLoading: Boolean = false,
    val isReporting: Boolean = false,
    val signal: SignalResponse? = null,
    val stats: StatsResponse? = null,
    val history: HistoryResponse? = null,
    val health: HealthResponse? = null,
    val batchResult: BatchResponse? = null,
    val error: String? = null,
    val selectedPair: String = "EUR/USD",
    val reportStatus: String? = null,
    val statsExpanded: Boolean = false,
    val historyExpanded: Boolean = false,
    val batchExpanded: Boolean = false,
    val healthExpanded: Boolean = false,
    val batchPair1: String = "EUR/USD",
    val batchPair2: String = "GBP/JPY",
    val batchPair3: String = "BTC/USD"
)
