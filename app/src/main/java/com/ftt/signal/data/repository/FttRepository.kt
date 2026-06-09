package com.ftt.signal.data.repository

import com.ftt.signal.data.api.FttApiService
import com.ftt.signal.data.model.BatchResponse
import com.ftt.signal.data.model.HealthResponse
import com.ftt.signal.data.model.HistoryResponse
import com.ftt.signal.data.model.ReportResponse
import com.ftt.signal.data.model.SignalResponse
import com.ftt.signal.data.model.StatsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FttRepository @Inject constructor(
    private val api: FttApiService
) {
    suspend fun health(): Result<HealthResponse> = runCatching { api.getHealth() }

    suspend fun signal(pair: String): Result<SignalResponse> = runCatching { api.getSignal(pair) }

    suspend fun batch(pairs: List<String>): Result<BatchResponse> = runCatching {
        api.getBatch(pairs.joinToString(","))
    }

    suspend fun history(pair: String, limit: Int = 10): Result<HistoryResponse> = runCatching {
        api.getHistory(pair, limit)
    }

    suspend fun stats(pair: String): Result<StatsResponse> = runCatching { api.getStats(pair) }

    suspend fun report(id: String, result: String): Result<ReportResponse> = runCatching {
        api.reportResult(id, result)
    }
}
