package com.javohir.feature.writing.trace

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import android.graphics.Path as AndroidPath

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Belgi konturini shrifdan olish.
 *
 * 62 ta harf/son uchun alohida vector asset saqlamaymiz — kontur Baloo2 Bold
 * shrifidan runtime'da olinadi.
 */
data class GlyphOutline(
    val path: Path,
    /** Konturning canvas'dagi haqiqiy chegarasi — stroke koordinatalari shunga moslanadi. */
    val bounds: Rect,
) {
    /** Normallashtirilgan (0..1) nuqtani canvas koordinatasiga o'tkazadi. */
    fun map(point: Offset): Offset = Offset(
        x = bounds.left + point.x * bounds.width,
        y = bounds.top + point.y * bounds.height,
    )

    fun map(points: List<Offset>): List<Offset> = points.map(::map)
}

/**
 * [char] konturini [typeface] dan olib, [size] ichiga (proporsiyani saqlab) joylaydi.
 * [padding] — chekkalardan qoldiriladigan bo'shliq (px).
 */
fun buildGlyphOutline(
    char: Char,
    typeface: Typeface,
    size: Size,
    padding: Float,
): GlyphOutline? {
    if (size.minDimension <= padding * 2) return null

    val (rawPath, rawBounds) = buildRawGlyphPath(char = char, typeface = typeface) ?: return null

    val available = Size(width = size.width - padding * 2, height = size.height - padding * 2)
    val scale = min(available.width / rawBounds.width(), available.height / rawBounds.height())
    val scaledWidth = rawBounds.width() * scale
    val scaledHeight = rawBounds.height() * scale
    val left = (size.width - scaledWidth) / 2f
    val top = (size.height - scaledHeight) / 2f

    // Konturni koordinata boshiga tortib, masshtablab, canvas markaziga qo'yamiz.
    val matrix = Matrix().apply {
        postTranslate(-rawBounds.left, -rawBounds.top)
        postScale(scale, scale)
        postTranslate(left, top)
    }
    rawPath.transform(matrix)

    return GlyphOutline(
        path = rawPath.asComposePath(),
        bounds = Rect(left = left, top = top, right = left + scaledWidth, bottom = top + scaledHeight),
    )
}

/**
 * [char] shaklini katak maskasiga aylantiradi — [GlyphSkeleton] yo'riqnoma chizig'ini shu
 * bo'yicha a'zoning o'rtasiga suradi.
 *
 * Maska belgi bounding box'i ustiga quriladi (ya'ni [GlyphOutline.map] bilan bir xil
 * normallashtirilgan koordinatada) va canvas o'lchamiga bog'liq emas — bir marta hisoblanib,
 * har o'lcham uchun ishlatiladi.
 */
fun buildGlyphField(char: Char, typeface: Typeface): GlyphField? {
    val (rawPath, rawBounds) = buildRawGlyphPath(char = char, typeface = typeface) ?: return null

    // Kataklar kvadrat bo'lishi uchun grid nisbati bounding box nisbatiga teng.
    val aspect = rawBounds.width() / rawBounds.height()
    val width: Int
    val height: Int
    if (aspect >= 1f) {
        width = FIELD_RESOLUTION
        height = max(1, (FIELD_RESOLUTION / aspect).roundToInt())
    } else {
        height = FIELD_RESOLUTION
        width = max(1, (FIELD_RESOLUTION * aspect).roundToInt())
    }

    rawPath.transform(
        Matrix().apply {
            postTranslate(-rawBounds.left, -rawBounds.top)
            postScale(width / rawBounds.width(), height / rawBounds.height())
        },
    )

    // ARGB_8888 + getPixels: ALPHA_8 dan farqli, satr to'ldirishi (stride) yo'q.
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Canvas(bitmap).drawPath(
        rawPath,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.BLACK
        },
    )

    val pixels = IntArray(width * height)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
    bitmap.recycle()

    // Harf ichidagi teshik ('A', 'O' ning o'rtasi) shrift path'ining fill turi hisobiga
    // bo'yalmay qoladi — maskada ham tashqari bo'lib chiqadi.
    return GlyphField(
        width = width,
        height = height,
        inside = BooleanArray(width * height) { index -> Color.alpha(pixels[index]) >= ALPHA_THRESHOLD },
    )
}

/** Shrifdagi belgi konturi va uning chegarasi — hech qanday masshtabsiz. */
private fun buildRawGlyphPath(char: Char, typeface: Typeface): Pair<AndroidPath, RectF>? {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        textSize = GLYPH_TEXT_SIZE
    }

    val path = AndroidPath()
    paint.getTextPath(char.toString(), 0, 1, 0f, 0f, path)

    val bounds = RectF()
    path.computeBounds(bounds, true)
    if (bounds.width() <= 0f || bounds.height() <= 0f) return null

    return path to bounds
}

/** Konturni px aniqligida olish uchun yetarlicha katta; keyin baribir masshtablanadi. */
private const val GLYPH_TEXT_SIZE = 512f

/** Maskaning eng uzun tomoni, katakda — chiziqni o'rtaga qo'yishga yetarli aniqlik. */
private const val FIELD_RESOLUTION = 192

/** Shundan past alfa — konturning silliqlangan cheti, shakldan tashqari. */
private const val ALPHA_THRESHOLD = 128