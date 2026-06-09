package com.ftt.signal.data.model

import com.google.gson.annotations.SerializedName

data class StatsResponse(
    @SerializedName("pair") val pair: String? = null,
    @SerializedName("stats") val stats: StatsData? = null,
    @SerializedName("error") val error: String? = null
)

data class StatsData(
    @SerializedName("winRate") val winRate: Double? = null,
    @SerializedName("totalSignals") val totalSignals: Int? = null,
    @SerializedName("wins") val wins: Int? = null,
    @SerializedName("losses") val losses: Int? = null,
    @SerializedName("lastUpdated") val lastUpdated: String? = null
)
