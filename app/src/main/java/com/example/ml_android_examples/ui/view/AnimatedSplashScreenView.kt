package com.example.ml_android_examples.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ml_android_examples.R

@Composable
fun AnimatedSplashScreenView(
    innerPadding: PaddingValues,
    onAnimationFinished: () -> Unit
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ding_sound))
    val lottieIsPlaying = remember { mutableStateOf(true) }

    val lottieProgress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = 2,
        isPlaying = lottieIsPlaying.value,
        speed = 1.0f,
        restartOnPlay = false
    )

    LaunchedEffect(lottieComposition) {
        if (lottieProgress == 1.0f) {
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(innerPadding)
            .background(
                Brush.Companion.linearGradient(
                    colors = listOf(Color(0xFF302E39), Color(0xFF252139)),
                    start = Offset.Companion.Zero,
                    end = Offset(1000f, 1000f)
                )
            ),
        contentAlignment = Alignment.Companion.Center
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = { lottieProgress },
            modifier = Modifier.Companion.fillMaxSize()
        )
    }

}