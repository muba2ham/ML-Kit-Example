package com.example.ml_android_examples.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ml_android_examples.nav.AppNavViews
import com.example.ml_android_examples.ui.theme.MLAndroidExamplesTheme
import com.example.ml_android_examples.ui.view.AnimatedSplashScreenView
import com.example.ml_android_examples.ui.view.MainAppView
import com.example.ml_android_examples.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        enableEdgeToEdge()

        setContent {
            MLAndroidExamplesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mainNavController = rememberNavController()

                    NavHost(
                        navController = mainNavController,
                        startDestination = AppNavViews.SplashNav.viewName
                    ) {
                        composable(AppNavViews.SplashNav.viewName) {
                            AnimatedSplashScreenView(
                                innerPadding,
                                onAnimationFinished = {
                                    mainNavController.navigate(AppNavViews.HomeNav.viewName) {
                                        popUpTo(AppNavViews.SplashNav.viewName) { inclusive = true }
                                    }
                                })
                        }
                        composable(AppNavViews.HomeNav.viewName) {
                            val mlKitNavController = rememberNavController()

                            MainAppView(innerPadding, mlKitNavController, mainActivityViewModel)
                        }
                    }

                }
            }
        }

    }

}