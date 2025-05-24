package com.example.ml_android_examples.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.util.UIStrings
import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.proofreading.Proofreader
import com.google.mlkit.genai.proofreading.ProofreadingRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.guava.await

@HiltViewModel
class ProofreadingViewModel @Inject constructor(
    private val proofreader: Proofreader
) : ViewModel(), ViewModelSharedContent {

    override val viewModelNumber = AppNavViews.ProofreadingNav.mlKitViewNumber

    private val _userInput = MutableStateFlow<String>("")
    val userInput: StateFlow<String> = _userInput

    private val _userResult = MutableStateFlow<String>("")
    val userResult: StateFlow<String> = _userResult

    override fun setUserInput(input: String) {
        _userInput.value = input
        _userResult.value = input
    }

    override fun processResult() {

        if (userInput.value.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                processProofreadingTask()
            }
        } else {
            _userResult.value = UIStrings.input_empty
        }

    }

    private suspend fun processProofreadingTask() {
        try {
            val featureStatusTask = proofreader.checkFeatureStatus().await()

            if (featureStatusTask == FeatureStatus.DOWNLOADABLE) {
                proofreader.downloadFeature(object : DownloadCallback {
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
                        viewModelScope.launch(Dispatchers.IO) {
                            startProofreadingRequest(userInput.value)
                        }
                    }
                })
            } else if (featureStatusTask == FeatureStatus.DOWNLOADING) {
                startProofreadingRequest(userInput.value)
            } else if (featureStatusTask == FeatureStatus.AVAILABLE) {
                startProofreadingRequest(userInput.value)
            }

        } catch (e: Exception) {
            _userResult.emit(e.message.toString())
        }
    }

    private suspend fun startProofreadingRequest(text: String) {
        val proofreadingRequest = ProofreadingRequest.builder(text).build()
        _userResult.value = proofreader.runInference(proofreadingRequest).await().results
            .joinToString(separator = ", ", prefix = "[", postfix = "]")
    }

    override fun onCleared() {
        Log.d("onClearedViewModel", "View Model OnCleared Called ProofreadingViewModel")
        proofreader.close()
    }
}