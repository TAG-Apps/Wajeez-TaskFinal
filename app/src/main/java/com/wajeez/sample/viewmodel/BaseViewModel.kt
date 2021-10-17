package com.wajeez.sample.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<N>: ViewModel() {

    val mIsLoading = ObservableBoolean(false)
     var mNavigator: N? = null

    open fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }

    open fun getNavigator(): N? {
        return mNavigator
    }

    open fun setNavigator(navigator: N) {
        mNavigator = navigator
    }
}