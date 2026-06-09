package com.ftt.signal.data.model

import com.google.gson.annotations.SerializedName

data class BatchResponse(
    @SerializedName("results") val results: List<SignalResponse>? = emptyList(),
    @SerializedName("error") val error: String? = null
)
