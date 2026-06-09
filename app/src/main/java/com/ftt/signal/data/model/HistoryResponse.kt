package com.ftt.signal.data.model

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("pair") val pair: String? = null,
    @SerializedName("total") val total: Int? = null,
    @SerializedName("showing") val showing: Int? = null,
    @SerializedName("winRate") val winRate: Double? = null,
    @SerializedName("signals") val signals: List<HistorySignal>? = emptyList(),
    @SerializedName("error") val error: String? = null
)

data class HistorySignal(
    @SerializedName("id") val id: String? = null,
    @SerializedName("direction") val direction: String? = null,
    @SerializedName("confidence") val confidence: Int? = null,
    @SerializedName("result") val result: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null
)
