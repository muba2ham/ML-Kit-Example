package com.example.ml_android_examples.di

import android.content.Context
import android.media.MediaPlayer
import com.example.ml_android_examples.R
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.genai.proofreading.ProofreaderOptions
import com.google.mlkit.genai.proofreading.Proofreading
import com.google.mlkit.genai.rewriting.RewriterOptions
import com.google.mlkit.genai.rewriting.Rewriting
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.SummarizerOptions
import com.google.mlkit.genai.summarization.SummarizerOptions.InputType
import com.google.mlkit.genai.summarization.SummarizerOptions.Language
import com.google.mlkit.genai.summarization.SummarizerOptions.OutputType
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelProviderModule {

    @Provides
    @ViewModelScoped
    fun getMediaPlayer(@ApplicationContext context: Context): MediaPlayer? = MediaPlayer.create(context, R.raw.ding_sound)

    @Provides
    @ViewModelScoped
    fun getSummarizerOptions(@ApplicationContext context: Context) = SummarizerOptions.builder(context)
        .setInputType(InputType.ARTICLE)
        .setOutputType(OutputType.ONE_BULLET)
        .setLanguage(Language.ENGLISH)
        .build()

    @Provides
    @ViewModelScoped
    fun getProofreaderOptions(@ApplicationContext context: Context) = ProofreaderOptions.builder(context)
        .setInputType(ProofreaderOptions.InputType.KEYBOARD)
        .setLanguage(ProofreaderOptions.Language.ENGLISH)
        .build()

    @Provides
    @ViewModelScoped
    fun getRewriterOptions(@ApplicationContext context: Context) = RewriterOptions.builder(context)
        .setOutputType(RewriterOptions.OutputType.REPHRASE)
        .setLanguage(RewriterOptions.Language.ENGLISH)
        .build()

    @Provides
    @ViewModelScoped
    fun getTranslatorOptions() = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.SPANISH)
        .build()

    @Provides
    @ViewModelScoped
    fun getSummarizerClient(summarizerOptions: SummarizerOptions) = Summarization.getClient(summarizerOptions)

    @Provides
    @ViewModelScoped
    fun getProofreaderClient(proofreaderOptions: ProofreaderOptions) = Proofreading.getClient(proofreaderOptions)

    @Provides
    @ViewModelScoped
    fun getRewriterClient(rewriterOptions: RewriterOptions) = Rewriting.getClient(rewriterOptions)

    @Provides
    @ViewModelScoped
    fun getLanguageClient() = LanguageIdentification.getClient(LanguageIdentificationOptions.Builder()
        .setConfidenceThreshold(0.5f)
        .build())

    @Provides
    @ViewModelScoped
    fun getEnglishSpanishTranslator(translatorOptions: TranslatorOptions) = Translation.getClient(translatorOptions)

    @Provides
    @ViewModelScoped
    fun getTranslatorDownloadConditions() = DownloadConditions.Builder().requireWifi().build()

    @Provides
    @ViewModelScoped
    fun getRemoteModelManager() = RemoteModelManager.getInstance()

    @Provides
    @ViewModelScoped
    fun getSmartReplyGenerator() = SmartReply.getClient()

}