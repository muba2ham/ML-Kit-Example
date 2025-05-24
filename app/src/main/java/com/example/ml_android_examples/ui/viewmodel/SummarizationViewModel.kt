package com.example.ml_android_examples.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.util.UIStrings
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.summarization.SummarizationRequest
import com.google.mlkit.genai.summarization.Summarizer
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.guava.await

@HiltViewModel
class SummarizationViewModel @Inject constructor(
    private val summarizer: Summarizer
) : ViewModel(), ViewModelSharedContent {

    override val viewModelNumber = AppNavViews.SummarizationNav.mlKitViewNumber

    private val _userInput = MutableStateFlow<String>("")
    val userInput: StateFlow<String> = _userInput

    private val _userResult = MutableStateFlow<String>("")
    val userResult: StateFlow<String> = _userResult

    override fun setUserInput(input: String) {
        Log.d("USER INPUT", input)
        _userInput.value = input
    }

    override fun processResult(){

        if (userInput.value.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                processSummarizationTask()
            }
        } else {
            _userResult.value = UIStrings.input_empty
        }

    }

    private suspend fun processSummarizationTask() {
        try {
            val featureStatusTask = summarizer.checkFeatureStatus().await()

            if (featureStatusTask == FeatureStatus.DOWNLOADABLE) {
                summarizer .downloadFeature(object : DownloadCallback {
                    override fun onDownloadStarted(bytesToDownload: Long) {
                        _userResult.value = "${UIStrings.download_start} $bytesToDownload"
                    }

                    override fun onDownloadFailed(e: GenAiException) {
                        _userResult.value = "${UIStrings.download_failed} ${e.message}"
                    }

                    override fun onDownloadProgress(totalBytesDownloaded: Long) {
                        _userResult.value = "${UIStrings.download_progress} $totalBytesDownloaded"
                    }

                    override fun onDownloadCompleted() {
                        startSummarizationRequest(userInput.value)
                    }
                })
            } else if (featureStatusTask == FeatureStatus.DOWNLOADING) {
                startSummarizationRequest(userInput.value)
            } else if (featureStatusTask == FeatureStatus.AVAILABLE) {
                startSummarizationRequest(userInput.value)
            }

        } catch (e: Exception) {
            _userResult.emit(e.message.toString())
        }
    }

    private fun startSummarizationRequest(text: String) {
        val summarizationRequest = SummarizationRequest.builder(text).build()
        _userResult.value = summarizer.runInference(summarizationRequest).get().summary
    }

    override fun onCleared() {
        Log.d("onClearedViewModel", "View Model OnCleared Called SummarizationViewModel")
        summarizer.close()
    }

}
