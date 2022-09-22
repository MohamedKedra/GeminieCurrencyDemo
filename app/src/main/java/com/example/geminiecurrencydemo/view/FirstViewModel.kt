package com.example.geminiecurrencydemo.view

import androidx.lifecycle.viewModelScope
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

    private val allSymbolsList = LiveDataState<ArrayList<String?>>()

    fun getAllSymbols(): LiveDataState<ArrayList<String?>> {

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

    private fun getCurrencyList(symbols: Symbols?): ArrayList<String?> {
        val list = ArrayList<String?>()
        list.add(symbols?.USD)
        list.add(symbols?.AED)
        list.add(symbols?.KWD)
        list.add(symbols?.EUR)
        list.add(symbols?.EGP)
        list.add(symbols?.JEP)
        list.add(symbols?.ALL)
        list.add(symbols?.BTC)
        list.add(symbols?.XAG)
        list.add(symbols?.GBP)
        return list
    }
}