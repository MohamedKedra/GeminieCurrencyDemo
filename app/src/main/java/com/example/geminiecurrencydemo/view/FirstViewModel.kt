package com.example.geminiecurrencydemo.view

import androidx.lifecycle.viewModelScope
import com.example.geminiecurrencydemo.network.models.ConvertCurrencyResponse
import com.example.geminiecurrencydemo.network.models.Currency
import com.example.geminiecurrencydemo.network.models.Symbols
import com.example.geminiecurrencydemo.repository.FirstRepository
import com.example.geminiecurrencydemo.utils.BaseViewModel
import com.example.geminiecurrencydemo.utils.ConnectionManager
import com.example.geminiecurrencydemo.utils.LiveDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val repository: FirstRepository,
    private val connectionManager: ConnectionManager
) :
    BaseViewModel() {

    private val allSymbolsList = LiveDataState<ArrayList<Currency>>()
    private val convertResponseData = LiveDataState<ConvertCurrencyResponse>()

    fun getAllSymbols(): LiveDataState<ArrayList<Currency>> {

        publishLoading(allSymbolsList)

        if (!connectionManager.isNetworkAvailable) {
            publishNoInternet(allSymbolsList)
            return allSymbolsList
        }

        viewModelScope.launch {
            val result = repository.getAllSymbols()
            if (result.isSuccessful) {
                publishResult(allSymbolsList, getCurrencyList(result.body()?.symbols))
            } else {
                publishError(allSymbolsList, Throwable(result.message()))
            }
        }

        return allSymbolsList
    }

    private fun getCurrencyList(symbols: Symbols?): ArrayList<Currency> {
        val list = ArrayList<Currency>()
        list.add(Currency(name = symbols?.USD.toString(), "USD"))
        list.add(Currency(name = symbols?.AED.toString(), "AED"))
        list.add(Currency(name = symbols?.EUR.toString(), "EUR"))
        list.add(Currency(name = symbols?.EGP.toString(), "EGP"))
        list.add(Currency(name = symbols?.GBP.toString(), "GBP"))
        list.add(Currency(name = symbols?.KWD.toString(), "KWD"))
        list.add(Currency(name = symbols?.JEP.toString(), "JEP"))
        return list
    }

    fun convertCurrency(
        from: String,
        to: String,
        amount: String
    ): LiveDataState<ConvertCurrencyResponse> {
        publishLoading(convertResponseData)

        if (!connectionManager.isNetworkAvailable) {
            publishNoInternet(convertResponseData)
            return convertResponseData
        }

        viewModelScope.launch {
            val result = repository.convertCurrency(from, to, amount)
            if (result.isSuccessful) {
                publishResult(convertResponseData, result.body())
            } else {
                publishError(convertResponseData, Throwable(result.message()))
            }
        }

        return convertResponseData
    }
}