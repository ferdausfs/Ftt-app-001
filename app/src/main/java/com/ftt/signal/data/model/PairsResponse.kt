package com.ftt.signal.data.model

import com.google.gson.annotations.SerializedName

data class PairsResponse(
    @SerializedName("pairs") val pairs: List<String>? = emptyList(),
    @SerializedName("forex") val forex: List<String>? = null,
    @SerializedName("crypto") val crypto: List<String>? = null,
    @SerializedName("otc") val otc: List<String>? = null,
    @SerializedName("error") val error: String? = null
)
