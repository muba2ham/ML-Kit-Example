package com.example.ml_android_examples.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.util.UIStrings
import com.google.mlkit.nl.languageid.LanguageIdentifier
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LanguageIdentificationViewModel @Inject constructor(
    private val languageIdentifier: LanguageIdentifier
) : ViewModel(), ViewModelSharedContent {

    override val viewModelNumber = AppNavViews.LanguageIdentificationNav.mlKitViewNumber

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
                languageIdentifier.identifyPossibleLanguages(userInput.value)
                    .addOnSuccessListener { identifiedLanguages ->
                        val resultBuilder = StringBuilder()
                        identifiedLanguages.forEach {
                            resultBuilder.append("{ ${UIStrings.language} ${it.languageTag} = ${UIStrings.confidence} ${it.confidence} }")
                        }
                        _userResult.value = resultBuilder.toString()
                    }
                    .addOnFailureListener {
                        _userResult.value = UIStrings.failed_languague_identification
                    }
            }
        } else {
            _userResult.value = UIStrings.input_empty
        }
    }

    override fun onCleared() {
        Log.d("onClearedViewModel", "LanguageIdentificationViewModel")
        languageIdentifier.close()
    }
}