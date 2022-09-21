package com.example.geminiecurrencydemo.network

import com.example.geminiecurrencydemo.network.models.SymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface GeminieService {

    @GET("symbols")
    suspend fun getAllSymbols(@Header("apikey") apiKey: String): Response<SymbolResponse>
}