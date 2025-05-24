package com.example.ml_android_examples.ui.viewmodel

interface ViewModelSharedContent {
    val viewModelNumber: Int
    fun setUserInput(input: String)
    fun processResult()
}