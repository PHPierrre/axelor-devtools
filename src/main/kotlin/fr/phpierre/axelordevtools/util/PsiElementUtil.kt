package fr.phpierre.axelordevtools.util

import com.intellij.psi.PsiElement

/**
 * https://intellij-support.jetbrains.com/hc/en-us/community/posts/206752355-The-dreaded-IntellijIdeaRulezzz-string
 */
class PsiElementUtil {

    companion object {
        fun getAutoCompleteValue(parameter: PsiElement): String {
            return removeIdeaRuleHack(parameter.text)
        }

        private fun removeIdeaRuleHack(value: String): String {
            return value.trim().replace("IntellijIdeaRulezzz", "")
        }
    }

}