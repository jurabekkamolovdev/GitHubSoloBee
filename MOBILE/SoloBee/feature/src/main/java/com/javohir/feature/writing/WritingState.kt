package com.javohir.feature.writing

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing
 * Description: UI State
 */
data class WritingState(
    val topicId: String = "",
    val subCategoryName: String = "",
    val score: String = "",
    val activityId: String = "",
    /** Backend yuborgan belgi ("A", "2"). Submit'da shu qaytariladi. */
    val char: String = "",
    val audioUrl: String = "",
    val completed: Boolean = false,
    val activities: List<WritingActivityItem> = emptyList(),
    /** Chizish qadamlari: harf uchun [A, a], son uchun [2]. */
    val steps: List<Char> = emptyList(),
    val stepIndex: Int = 0,
    /** Har muvaffaqiyatsiz submit'da ortadi — chizishni noldan boshlash uchun kalit. */
    val attempt: Int = 0,
    val isLoadingProfile: Boolean = false,
    val isLoadingActivity: Boolean = false,
    val isSubmitting: Boolean = false,
) {
    /** Hozir chizilayotgan belgi. Barcha qadamlar tugagach null. */
    val currentChar: Char? get() = steps.getOrNull(stepIndex)

    /** Barcha qadamlar chizilgan — submit qilsa bo'ladi. */
    val allStepsDone: Boolean get() = steps.isNotEmpty() && stepIndex > steps.lastIndex

    companion object {
        /**
         * Harf ikki qadam: avval bosh, keyin kichik. Son — bitta qadam.
         * Bosh va kichik shakli bir xil bo'lgan belgilar (masalan raqamlar) takrorlanmaydi.
         */
        fun stepsOf(char: String): List<Char> {
            val first = char.trim().firstOrNull() ?: return emptyList()
            val upper = first.uppercaseChar()
            val lower = first.lowercaseChar()
            return if (upper == lower) listOf(first) else listOf(upper, lower)
        }
    }
}

data class WritingActivityItem(
    val id: String,
    val title: String,
    val enabled: Boolean,
    val completed: Boolean,
)
