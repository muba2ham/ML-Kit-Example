package com.example.ml_android_examples.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.util.UIStrings
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translator
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TranslationViewModel @Inject constructor(
    private val translator: Translator,
    private val downloadConditions: DownloadConditions,
    private val remoteModelManager: RemoteModelManager
) : ViewModel(), ViewModelSharedContent {

    override val viewModelNumber = AppNavViews.TranslationNav.mlKitViewNumber

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
                remoteModelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                    .addOnSuccessListener { models ->
                        if (models.isEmpty()) {
                            translator.downloadModelIfNeeded(downloadConditions)
                                .addOnSuccessListener {
                                    translator.translate(userInput.value)
                                    .addOnSuccessListener { translatedText ->
                                        _userResult.value = translatedText
                                    }
                                    .addOnFailureListener { exception ->
                                        _userResult.value = UIStrings.translator_failed_translation
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    _userResult.value = UIStrings.translator_download_failed
                                }
                        } else {
                            models.forEach {
                                translator.translate(userInput.value)
                                    .addOnSuccessListener { translatedText ->
                                        _userResult.value = translatedText
                                    }
                                    .addOnFailureListener { exception ->
                                        _userResult.value = UIStrings.translator_failed_translation
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        _userResult.value = UIStrings.translator_downloaded_failed
                    }
            }
        } else {
            _userResult.value = UIStrings.input_empty
        }
    }

    override fun onCleared() {
        Log.d("onClearedViewModel", "View Model OnCleared Called TranslationViewModel")
        translator.close()
    }
}