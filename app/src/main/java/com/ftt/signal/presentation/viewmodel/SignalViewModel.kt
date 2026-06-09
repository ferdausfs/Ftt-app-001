package com.ftt.signal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftt.signal.data.repository.FttRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignalViewModel @Inject constructor(
    private val repo: FttRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignalUiState())
    val uiState: StateFlow<SignalUiState> = _uiState.asStateFlow()

    init {
        checkHealth()
    }

    fun selectPair(pair: String) {
        _uiState.update { it.copy(selectedPair = pair) }
    }

    fun setBatchPair(index: Int, pair: String) {
        _uiState.update {
            when (index) {
                0 -> it.copy(batchPair1 = pair)
                1 -> it.copy(batchPair2 = pair)
                else -> it.copy(batchPair3 = pair)
            }
        }
    }

    fun toggleStats() = _uiState.update {
        val newVal = !it.statsExpanded
        it.copy(statsExpanded = newVal)
    }.also {
        if (_uiState.value.statsExpanded && _uiState.value.stats == null) loadStats()
    }

    fun toggleHistory() = _uiState.update {
        val newVal = !it.historyExpanded
        it.copy(historyExpanded = newVal)
    }.also {
        if (_uiState.value.historyExpanded && _uiState.value.history == null) loadHistory()
    }

    fun toggleBatch() = _uiState.update { it.copy(batchExpanded = !it.batchExpanded) }

    fun toggleHealth() = _uiState.update { it.copy(healthExpanded = !it.healthExpanded) }

    fun clearError() = _uiState.update { it.copy(error = null) }

    fun clearReportStatus() = _uiState.update { it.copy(reportStatus = null) }

    fun fetchSignal() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, signal = null) }
            val pair = _uiState.value.selectedPair
            repo.signal(pair)
                .onSuccess { resp ->
                    _uiState.update { it.copy(isLoading = false, signal = resp) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = friendlyError(e)) }
                }
        }
    }

    fun checkHealth() {
        viewModelScope.launch {
            _uiState.update { it.copy(isHealthLoading = true) }
            repo.health()
                .onSuccess { resp ->
                    _uiState.update { it.copy(isHealthLoading = false, health = resp) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isHealthLoading = false, error = friendlyError(e))
                    }
                }
        }
    }

    fun loadStats() {
        viewModelScope.launch {
            _uiState.update { it.copy(isStatsLoading = true) }
            repo.stats(_uiState.value.selectedPair)
                .onSuccess { resp ->
                    _uiState.update { it.copy(isStatsLoading = false, stats = resp) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isStatsLoading = false, error = friendlyError(e))
                    }
                }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isHistoryLoading = true) }
            repo.history(_uiState.value.selectedPair, 10)
                .onSuccess { resp ->
                    _uiState.update { it.copy(isHistoryLoading = false, history = resp) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isHistoryLoading = false, error = friendlyError(e))
                    }
                }
        }
    }

    fun fetchBatch() {
        viewModelScope.launch {
            val state = _uiState.value
            val pairs = listOf(state.batchPair1, state.batchPair2, state.batchPair3)
                .filter { it.isNotBlank() }
            _uiState.update { it.copy(isBatchLoading = true, batchResult = null, error = null) }
            repo.batch(pairs)
                .onSuccess { resp ->
                    _uiState.update { it.copy(isBatchLoading = false, batchResult = resp) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isBatchLoading = false, error = friendlyError(e))
                    }
                }
        }
    }

    fun reportResult(result: String) {
        val id = _uiState.value.signal?.signal?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isReporting = true) }
            repo.report(id, result)
                .onSuccess {
                    _uiState.update {
                        it.copy(isReporting = false, reportStatus = "Result recorded ✓")
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isReporting = false, error = friendlyError(e))
                    }
                }
        }
    }

    private fun friendlyError(e: Throwable): String {
        val msg = e.message.orEmpty()
        return when {
            msg.contains("Unable to resolve host", true) ||
                msg.contains("failed to connect", true) ||
                msg.contains("timeout", true) ->
                "Connection failed. Check internet."
            else -> msg.ifBlank { "Unexpected error" }
        }
    }
}
