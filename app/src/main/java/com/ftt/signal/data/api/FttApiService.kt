package com.ftt.signal.data.api

import com.ftt.signal.data.model.BatchResponse
import com.ftt.signal.data.model.HealthResponse
import com.ftt.signal.data.model.HistoryResponse
import com.ftt.signal.data.model.PairsResponse
import com.ftt.signal.data.model.ReportResponse
import com.ftt.signal.data.model.SignalResponse
import com.ftt.signal.data.model.StatsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FttApiService {

    @GET("health")
    suspend fun getHealth(): HealthResponse

    @GET("/")
    suspend fun getRoot(): HealthResponse

    @GET("api/signal")
    suspend fun getSignal(@Query("pair") pair: String): SignalResponse

    @GET("api/batch")
    suspend fun getBatch(@Query("pairs") pairs: String): BatchResponse

    @GET("api/pairs")
    suspend fun getPairs(): PairsResponse

    @GET("api/history")
    suspend fun getHistory(
        @Query("pair") pair: String,
        @Query("limit") limit: Int = 20
    ): HistoryResponse

    @GET("api/stats")
    suspend fun getStats(@Query("pair") pair: String): StatsResponse

    @GET("api/report")
    suspend fun reportResult(
        @Query("id") id: String,
        @Query("result") result: String
    ): ReportResponse
}
