package com.example.ml_android_examples.nav

import com.example.ml_android_examples.util.UIStrings
import kotlinx.serialization.Serializable

@Serializable
sealed class AppNavViews(val viewName: String, val mlKitViewNumber: Int) {

    @Serializable object SplashNav : AppNavViews(UIStrings.header_home, 0)
    @Serializable object HomeNav : AppNavViews(UIStrings.header_home, 0)

    @Serializable object SummarizationNav : AppNavViews(UIStrings.header_summarization, 1)
    @Serializable object ProofreadingNav : AppNavViews(UIStrings.header_proofreading, 2)
    @Serializable object RewritingNav : AppNavViews(UIStrings.header_rewriting, 3)
    @Serializable object LanguageIdentificationNav : AppNavViews(UIStrings.header_language_identification, 4)
    @Serializable object TranslationNav : AppNavViews(UIStrings.header_translation, 5)
    @Serializable object SmartReplyNav : AppNavViews(UIStrings.header_smart_reply, 6)

    companion object {
        fun getMlKitViewName(mlKitViewNumber: Int): AppNavViews? {
            return when (mlKitViewNumber) {
                SummarizationNav.mlKitViewNumber -> SummarizationNav
                ProofreadingNav.mlKitViewNumber -> ProofreadingNav
                RewritingNav.mlKitViewNumber -> RewritingNav
                LanguageIdentificationNav.mlKitViewNumber -> LanguageIdentificationNav
                TranslationNav.mlKitViewNumber -> TranslationNav
                SmartReplyNav.mlKitViewNumber -> SmartReplyNav
                else -> null
            }
        }
    }
}