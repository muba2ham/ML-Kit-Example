package com.example.ml_android_examples.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ml_android_examples.model.ViewModelType
import com.example.ml_android_examples.ui.viewmodel.LanguageIdentificationViewModel
import com.example.ml_android_examples.ui.viewmodel.ProofreadingViewModel
import com.example.ml_android_examples.ui.viewmodel.RewritingTextViewModel
import com.example.ml_android_examples.ui.viewmodel.SmartReplyViewModel
import com.example.ml_android_examples.ui.viewmodel.SummarizationViewModel
import com.example.ml_android_examples.ui.viewmodel.TranslationViewModel
import com.example.ml_android_examples.ui.viewmodel.ViewModelSharedContent

@Composable
fun <T : ViewModelSharedContent> MlContainerView(
    headerText: String,
    headerTextPlaceHolder: String,
    headerResult: String,
    headerResultPlaceHolder: String,
    viewModelType: ViewModelType<T>
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()

    var userInput by remember { mutableStateOf("") }
    var userResult by remember { mutableStateOf("") }

    when (viewModelType.viewModel) {
        is SummarizationViewModel -> {
            userInput = viewModelType.viewModel.userInput.collectAsState().value
            userResult = viewModelType.viewModel.userResult.collectAsState().value
        }
        is ProofreadingViewModel -> {
            userInput = viewModelType.viewModel.userInput.collectAsState().value
            userResult = viewModelType.viewModel.userResult.collectAsState().value
        }
        is RewritingTextViewModel -> {
            userInput = viewModelType.viewModel.userInput.collectAsState().value
            userResult = viewModelType.viewModel.userResult.collectAsState().value
        }
        is LanguageIdentificationViewModel -> {
            userInput = viewModelType.viewModel.userInput.collectAsState().value
            userResult = viewModelType.viewModel.userResult.collectAsState().value
        }
        is TranslationViewModel -> {
            userInput = viewModelType.viewModel.userInput.collectAsState().value
            userResult = viewModelType.viewModel.userResult.collectAsState().value
        }
        is SmartReplyViewModel -> {
            userInput = viewModelType.viewModel.userInput.collectAsState().value
            userResult = viewModelType.viewModel.userResult.collectAsState().value
        }
    }

    var showResult by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = headerText,
                onValueChange = { viewModelType.viewModel.setUserInput(it) },
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0F7FA))
                    .clickable(
                        enabled = true,
                        onClick = {
                            showResult = false
                        }
                    ),
                trailingIcon = {
                    Icon(
                        active = !showResult,
                        activeContent = { },
                        inactiveContent = {
                            androidx.compose.material3.Icon(Icons.Filled.KeyboardArrowDown,
                                contentDescription = null, tint = Color.Black) }
                    )
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledContainerColor = Color(0xFFE0F7FA),
                    disabledIndicatorColor = Color.Black,
                    disabledLabelColor = Color(0xFFE0F7FA),
                    disabledPlaceholderColor = Color.Black
                )
            )
        }

        if (!showResult) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = userInput,
                onValueChange = { viewModelType.viewModel.setUserInput(it) },
                label = { Text(headerTextPlaceHolder) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // Hide keyboard when done
                    }
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = headerResult,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF9C4))
                    .clickable(
                        enabled = true,
                        onClick = {
                            if (!showResult && userInput.isNotBlank()) {
                                viewModelType.viewModel.processResult()
                            }

                            showResult = true
                        }
                    ),
                trailingIcon = {
                    Icon(
                        active = showResult,
                        activeContent = { },
                        inactiveContent = {
                            androidx.compose.material3.Icon(Icons.Filled.KeyboardArrowUp,
                                contentDescription = null, tint = Color.Black) }
                    )
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledContainerColor = Color(0xFFFFF9C4),
                    disabledIndicatorColor = Color.Black,
                    disabledLabelColor = Color(0xFFFFF9C4),
                    disabledPlaceholderColor = Color.Black
                )
            )
        }

        if (showResult) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = userResult,
                onValueChange = {},
                label = { Text(headerResultPlaceHolder) },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}