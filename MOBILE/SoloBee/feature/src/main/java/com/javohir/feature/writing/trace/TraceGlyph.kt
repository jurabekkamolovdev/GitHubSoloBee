package com.javohir.feature.writing.trace

import androidx.compose.ui.geometry.Offset

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Belgini chizish yo'riqnomasi — stroke'lar tartibi va yo'nalishi.
 */
data class TraceGlyph(val strokes: List<TraceStroke>)

/**
 * Bitta uzluksiz chiziq. [points] — belgi tashqi konturining o'z bounding box'iga
 * nisbatan normallashtirilgan (0..1, y pastga) siniq chiziq.
 */
data class TraceStroke(
    val points: List<Offset>,
    private val badgeOverride: Offset? = null,
    /**
     * `true` bo'lsa chiziq [GlyphSkeleton] orqali shrift shakliga (a'zo o'rta chizig'iga)
     * yopishtiriladi. `false` bo'lsa [points] o'zi ishlatiladi — chiziq qo'lda o'lchab
     * joylashtirilgan bo'lishi kerak. 'Q' halqasi kabi a'zo tugunga (dumcha) ulanadigan
     * joyda snap chiziqni tortib buzadi, shu sababli u yerda snap o'chiriladi.
     */
    val snapToShape: Boolean = true,
) {
    val start: Offset get() = points.first()
    val end: Offset get() = points.last()

    val badgeAt: Offset get() = badgeOverride ?: start
}
