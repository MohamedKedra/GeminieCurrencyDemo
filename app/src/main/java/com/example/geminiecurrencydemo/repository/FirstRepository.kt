package com.example.geminiecurrencydemo.repository

import com.example.geminiecurrencydemo.network.GeminieService
import com.example.geminiecurrencydemo.utils.Constant
import javax.inject.Inject

class FirstRepository @Inject constructor(private val service: GeminieService) {
    suspend fun getAllSymbols() = service.getAllSymbols(Constant.ApiKey)
}