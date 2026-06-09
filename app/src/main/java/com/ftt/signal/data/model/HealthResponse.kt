package com.ftt.signal.data.model

import com.google.gson.annotations.SerializedName

data class HealthResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("version") val version: String? = null,
    @SerializedName("currentSession") val currentSession: String? = null,
    @SerializedName("apiKeysConfigured") val apiKeysConfigured: Int? = null,
    @SerializedName("kvCache") val kvCache: String? = null,
    @SerializedName("markets") val markets: MarketsData? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("error") val error: String? = null
)

data class MarketsData(
    @SerializedName("forex") val forex: String? = null,
    @SerializedName("crypto") val crypto: String? = null
)
