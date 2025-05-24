package com.example.ml_android_examples.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ml_android_examples.model.ViewModelType
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.ui.viewmodel.LanguageIdentificationViewModel
import com.example.ml_android_examples.ui.viewmodel.MainActivityViewModel
import com.example.ml_android_examples.ui.viewmodel.ProofreadingViewModel
import com.example.ml_android_examples.ui.viewmodel.RewritingTextViewModel
import com.example.ml_android_examples.ui.viewmodel.SmartReplyViewModel
import com.example.ml_android_examples.ui.viewmodel.SummarizationViewModel
import com.example.ml_android_examples.ui.viewmodel.TranslationViewModel
import com.example.ml_android_examples.util.UIStrings

@Composable
fun MainAppView(
    innerPadding: PaddingValues,
    mlKitNavController: NavHostController,
    mainActivityViewModel: MainActivityViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF302E39), Color(0xFF252139)),
                    start = Offset.Zero,
                    end = Offset(1000f, 1000f)
                )
            )
            .padding(innerPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(top = 3.dp),
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            text = mainActivityViewModel.headerName.collectAsState().value,
            style = MaterialTheme.typography.headlineSmall
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 3.dp, bottom = 3.dp, start = 7.dp, end = 7.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White)
        ) {
            NavHost(
                navController = mlKitNavController,
                startDestination = AppNavViews.SummarizationNav.viewName,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(AppNavViews.SummarizationNav.viewName) {
                    mainActivityViewModel.setHeaderName(AppNavViews.SummarizationNav.viewName)
                    val viewModel: SummarizationViewModel = hiltViewModel()
                    MlContainerView(
                        UIStrings.header_summarization_text,
                        UIStrings.header_summarization_text_placeholder,
                        UIStrings.header_summarization_result,
                        UIStrings.header_summarization_result_placeholder,
                        ViewModelType(viewModel)
                    )
                }
                composable(AppNavViews.ProofreadingNav.viewName) {
                    mainActivityViewModel.setHeaderName(AppNavViews.ProofreadingNav.viewName)
                    val viewModel: ProofreadingViewModel = hiltViewModel()
                    MlContainerView(
                        UIStrings.header_proofreading_text,
                        UIStrings.header_proofreading_text_placeholder,
                        UIStrings.header_proofreading_result,
                        UIStrings.header_proofreading_result_placeholder,
                        ViewModelType(viewModel)
                    )
                }
                composable(AppNavViews.RewritingNav.viewName) {
                    mainActivityViewModel.setHeaderName(AppNavViews.RewritingNav.viewName)
                    val viewModel: RewritingTextViewModel = hiltViewModel()
                    MlContainerView(
                        UIStrings.header_rewriting_text,
                        UIStrings.header_rewriting_text_placeholder,
                        UIStrings.header_rewriting_result,
                        UIStrings.header_rewriting_result_placeholder,
                        ViewModelType(viewModel)
                    )
                }
                composable(AppNavViews.LanguageIdentificationNav.viewName) {
                    mainActivityViewModel.setHeaderName(AppNavViews.LanguageIdentificationNav.viewName)
                    val viewModel: LanguageIdentificationViewModel = hiltViewModel()
                    MlContainerView(
                        UIStrings.header_language_identification_text,
                        UIStrings.header_language_identification_text_placeholder,
                        UIStrings.header_language_identification_result,
                        UIStrings.header_language_identification_result_placeholder,
                        ViewModelType(viewModel)
                    )
                }
                composable(AppNavViews.TranslationNav.viewName) {
                    mainActivityViewModel.setHeaderName(AppNavViews.TranslationNav.viewName)
                    val viewModel: TranslationViewModel = hiltViewModel()
                    MlContainerView(
                        UIStrings.header_translation_text,
                        UIStrings.header_translation_text_placeholder,
                        UIStrings.header_translation_result,
                        UIStrings.header_translation_result_placeholder,
                        ViewModelType(viewModel)
                    )
                }
                composable(AppNavViews.SmartReplyNav.viewName) {
                    mainActivityViewModel.setHeaderName(AppNavViews.SmartReplyNav.viewName)
                    val viewModel: SmartReplyViewModel = hiltViewModel()
                    MlContainerView(
                        UIStrings.header_smart_reply_text,
                        UIStrings.header_smart_reply_text_placeholder,
                        UIStrings.header_smart_reply_result,
                        UIStrings.header_smart_reply_result_placeholder,
                        ViewModelType(viewModel)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            contentAlignment = Alignment.Center
        ) {
            MlKitNavLeftRightView(
                onSwipeLeft = {
                    val current = mainActivityViewModel.mlKitViewNumber.value
                    val moveLeft: AppNavViews? = AppNavViews.getMlKitViewName(current-1)

                    if (moveLeft != null) {
                        mainActivityViewModel.playDing()
                        mainActivityViewModel.setMLKitViewNumber(moveLeft.mlKitViewNumber)

                        mlKitNavController.navigate(moveLeft.viewName) {
                            popUpTo(AppNavViews.getMlKitViewName(current)!!.viewName) { inclusive = true }
                        }
                    }
                },
                onSwipeRight = {
                    val current = mainActivityViewModel.mlKitViewNumber.value
                    val moveRight: AppNavViews? = AppNavViews.getMlKitViewName(current+1)

                    if (moveRight != null) {
                        mainActivityViewModel.playDing()
                        mainActivityViewModel.setMLKitViewNumber(moveRight.mlKitViewNumber)

                        mlKitNavController.navigate(moveRight.viewName) {
                            popUpTo(AppNavViews.getMlKitViewName(current)!!.viewName) { inclusive = true }
                        }
                    }
                }
            )
        }

    }

}