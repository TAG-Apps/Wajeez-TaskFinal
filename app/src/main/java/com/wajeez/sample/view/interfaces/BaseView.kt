package com.wajeez.sample.view.interfaces

interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun showAlert(error: String?)
}