package com.javohir.feature.test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.test
 * Description: UI State
 */
data class TestState(
    val topicId: String = "",
    val subCategoryName: String = "",
    val score: String = "",
    val activityId: String = "",
    val answer: String = "",
    val imageUrl: String = "",
    val audioUrl: String = "",
    val completed: Boolean = false,
    val options: List<TestLetterOption> = emptyList(),
    val typed: List<TypedLetter> = emptyList(),
    val activities: List<TestActivityItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingProfile: Boolean = false,
    val isLoadingActivity: Boolean = false,
    val isSubmitting: Boolean = false,
) {
    val typedText: String get() = typed.joinToString(separator = "") { it.char }

    /**
     * Barcha kataklar to'ldirilganmi. Javob (answer) bilan solishtirilmaydi —
     * to'g'rilikni server tekshiradi.
     */
    val isFullyTyped: Boolean
        get() = answer.isNotBlank() && typed.size >= answer.length
}

data class TestLetterOption(
    val index: Int,
    val char: String,
    val imageUrl: String?,
    val used: Boolean = false,
)

data class TypedLetter(
    val optionIndex: Int,
    val char: String,
)

data class TestActivityItem(
    val id: String,
    val title: String,
    val enabled: Boolean,
    val completed: Boolean,
)