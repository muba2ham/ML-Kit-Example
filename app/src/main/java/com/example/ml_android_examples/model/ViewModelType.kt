package com.example.ml_android_examples.model

import com.example.ml_android_examples.ui.viewmodel.ViewModelSharedContent

data class ViewModelType<T : ViewModelSharedContent>(
    val viewModel: T, val viewModelNumber: Int = viewModel.viewModelNumber)