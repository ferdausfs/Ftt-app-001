package com.ftt.signal.data.model

import com.google.gson.annotations.SerializedName

data class SignalResponse(
    @SerializedName("pair") val pair: String? = null,
    @SerializedName("assetType") val assetType: String? = null,
    @SerializedName("marketStatus") val marketStatus: String? = null,
    @SerializedName("session") val session: SessionData? = null,
    @SerializedName("signal") val signal: SignalData? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("nextOpen") val nextOpen: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null
)

data class SignalData(
    @SerializedName("direction") val direction: String? = null,
    @SerializedName("confidence") val confidence: Int? = null,
    @SerializedName("duration") val duration: Int? = null,
    @SerializedName("timeframe") val timeframe: String? = null,
    @SerializedName("entryPrice") val entryPrice: Double? = null,
    @SerializedName("sl") val sl: Double? = null,
    @SerializedName("tp") val tp: Double? = null,
    @SerializedName("reasoning") val reasoning: String? = null,
    @SerializedName("id") val id: String? = null
)

data class SessionData(
    @SerializedName("name") val name: String? = null,
    @SerializedName("quality") val quality: String? = null
)
