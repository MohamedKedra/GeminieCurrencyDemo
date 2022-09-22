package com.example.geminiecurrencydemo.utils

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private var requestInProgress = false

    fun <T> publishLoading(liveData: LiveDataState<T>) {
        requestInProgress = true
        liveData.postLoading()
    }

    fun <T> publishNoInternet(liveData: LiveDataState<T>) {
        requestInProgress = false
        liveData.postNoInternet()
    }

    fun <T> publishError(liveData: LiveDataState<T>, t: Throwable) {
        requestInProgress = false
        liveData.postError(t)
    }

    fun <T> publishResult(liveData: LiveDataState<T>, data: T?) {
        requestInProgress = false
        liveData.postSuccess(data)
    }
}