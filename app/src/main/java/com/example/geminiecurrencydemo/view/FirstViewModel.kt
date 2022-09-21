package com.example.geminiecurrencydemo.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminiecurrencydemo.network.models.SymbolResponse
import com.example.geminiecurrencydemo.repository.FirstRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(private val repository: FirstRepository) : ViewModel() {

    private val symbolsList = MutableLiveData<SymbolResponse?>()

    fun getAllSymbols(): MutableLiveData<SymbolResponse?> {

        viewModelScope.launch {

            val result = repository.getAllSymbols()
            if (result.isSuccessful) {
                symbolsList.postValue(result.body())
            } else {
                symbolsList.postValue(null)
            }

        }

        return symbolsList
    }
}