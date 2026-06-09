package com.ftt.signal.data.model

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("result") val result: String? = null,
    @SerializedName("error") val error: String? = null
)
