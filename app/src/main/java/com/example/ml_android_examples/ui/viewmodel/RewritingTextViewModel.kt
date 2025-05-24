package com.example.ml_android_examples.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.util.UIStrings
import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.rewriting.Rewriter
import com.google.mlkit.genai.rewriting.RewritingRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.guava.await

@HiltViewModel
class RewritingTextViewModel @Inject constructor(
    private val rewriter: Rewriter
) : ViewModel(), ViewModelSharedContent {

    override val viewModelNumber = AppNavViews.RewritingNav.mlKitViewNumber

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
                processRewriteTask()
            }
        } else {
            _userResult.value = UIStrings.input_empty
        }
    }

    private suspend fun processRewriteTask() {
        try {
            val featureStatusTask = rewriter.checkFeatureStatus().await()

            if (featureStatusTask == FeatureStatus.DOWNLOADABLE) {
                rewriter.downloadFeature(object : DownloadCallback {
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
                            startRewritingRequest(userInput.value)
                        }
                    }
                })
            } else if (featureStatusTask == FeatureStatus.DOWNLOADING) {
                startRewritingRequest(userInput.value)
            } else if (featureStatusTask == FeatureStatus.AVAILABLE) {
                startRewritingRequest(userInput.value)
            }

        } catch (e: Exception) {
            _userResult.emit(e.message.toString())
        }
    }

    private suspend fun startRewritingRequest(text: String) {
        val rewritingRequest = RewritingRequest.builder(text).build()
        _userResult.value =
            rewriter.runInference(rewritingRequest).await().results
                .joinToString(separator = ", ", prefix = "[", postfix = "]")
    }

    override fun onCleared() {
        Log.d("onClearedViewModel", "View Model OnCleared Called RewritingTextViewModel")
        rewriter.close()
    }
}