package com.example.geminiecurrencydemo.network.models

data class ConvertCurrencyResponse(
    val date: String?,
    val info: Info?,
    val query: Query?,
    val result: Double?,
    val success: Boolean?
)

data class Query(
    val amount: Int?,
    val from: String?,
    val to: String?
)

data class Info(
    val rate: Double?,
    val timestamp: Int?
)