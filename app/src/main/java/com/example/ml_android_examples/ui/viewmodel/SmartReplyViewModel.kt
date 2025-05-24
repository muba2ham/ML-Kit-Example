package com.example.ml_android_examples.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.util.UIStrings
import com.google.mlkit.nl.smartreply.SmartReplyGenerator
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class SmartReplyViewModel @Inject constructor(
    private val smartReplyGenerator: SmartReplyGenerator
) : ViewModel(), ViewModelSharedContent {

    val userID = UUID.randomUUID().toString()

    override val viewModelNumber = AppNavViews.SmartReplyNav.mlKitViewNumber

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
                smartReplyGenerator.suggestReplies(listOf(TextMessage.createForRemoteUser(userInput.value, System.currentTimeMillis(), userID)))
                    .addOnSuccessListener { result ->
                        if (result.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                            _userResult.value = UIStrings.smart_reply_not_supported
                        } else if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            val resultBuilder = StringBuilder()
                            result.suggestions.forEachIndexed { index, suggestion ->
                                resultBuilder.append("{ $index : $suggestion }")
                            }
                            _userResult.value = resultBuilder.toString()
                        }
                    }
                    .addOnFailureListener {
                        _userResult.value = UIStrings.smart_reply_failed
                    }
            }
        } else {
            _userResult.value = UIStrings.input_empty
        }

    }

    override fun onCleared() {
        Log.d("onClearedViewModel", "View Model OnCleared Called SmartReplyViewModel")
        smartReplyGenerator.close()
    }
}