plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.dagger)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.example.ml_android_examples"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ml_android_examples"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.retrofit.logger)
    implementation(libs.nav.compose)
    implementation(libs.serialization)
    implementation(libs.security.crypto)
    implementation(libs.security.app.authenticator)
    implementation(libs.security.identity.credential)
    implementation(libs.splashscreen)
    implementation(libs.lottie.compose)
    implementation(libs.hilt.nav)
    implementation(libs.coroutines.play.services)
    implementation(libs.coroutines.guava)
    implementation(libs.genai.summarization)
    implementation(libs.genai.proofreading)
    implementation(libs.genai.rewriting)
    implementation(libs.mlkit.language)
    implementation(libs.mlkit.translate)
    implementation(libs.mlkit.smart.reply)

}