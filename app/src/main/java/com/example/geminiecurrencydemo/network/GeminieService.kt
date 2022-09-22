package com.example.geminiecurrencydemo.network

import com.example.geminiecurrencydemo.network.models.ConvertCurrencyResponse
import com.example.geminiecurrencydemo.network.models.SymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeminieService {

    @GET("symbols")
    suspend fun getAllSymbols(@Header("apikey") apiKey: String): Response<SymbolResponse>

    @GET("convert")
    suspend fun convertCurrency(
        @Header("apikey") apiKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): Response<ConvertCurrencyResponse>
}