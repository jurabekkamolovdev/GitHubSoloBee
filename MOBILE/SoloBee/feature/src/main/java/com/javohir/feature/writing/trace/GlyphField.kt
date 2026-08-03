package com.javohir.feature.writing.trace

import androidx.compose.ui.geometry.Offset
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Belgi shaklining qalinlik xaritasi.
 *
 * Har katak uchun konturgacha bo'lgan ISHORALI masofani saqlaydi: harf ichida musbat
 * (a'zoning o'rtasida eng katta), konturda 0, tashqarida manfiy. [GlyphSkeleton] shu
 * xarita bo'yicha yo'riqnoma chizig'ini a'zoning aniq o'rtasiga suradi.
 *
 * Tashqarida ham qiymat bo'lishi muhim: taxminiy chiziq harfdan chiqib ketgan bo'lsa,
 * manfiy tomondan ko'tarilib o'zi ichkariga qaytadi. Faqat "ichkari/tashqari" bilan bunday
 * nuqta hech qayerga siljimasdi.
 *
 * Kataklar kvadrat — [width]/[height] belgi bounding box'ining nisbatiga teng qilib
 * tanlanadi. Shu sababli masofa har yo'nalishda bir xil o'lchovda va perpendikulyar
 * haqiqatan ham perpendikulyar bo'ladi. Hisob-kitob shu katak koordinatasida ("grid")
 * boradi, normallashtirilgan 0..1 da emas.
 *
 * Android'ga bog'liq emas — shakl [inside] maskasi ko'rinishida beriladi.
 */
class GlyphField(
    val width: Int,
    val height: Int,
    inside: BooleanArray,
) {
    init {
        require(width > 0 && height > 0) { "Bo'sh grid: ${width}x$height" }
        require(inside.size == width * height) {
            "Maska o'lchami mos emas: ${inside.size} != ${width * height}"
        }
    }

    /** Eng uzun tomon — masofa chegaralarini belgi kattaligiga nisbatan berish uchun. */
    val longestSide: Int get() = max(width, height)

    private val distance: FloatArray = signedDistance(width = width, height = height, inside = inside)

    /** Normallashtirilgan (0..1) nuqtani katak koordinatasiga o'tkazadi. */
    fun toGrid(point: Offset): Offset = Offset(x = point.x * width, y = point.y * height)

    /** Katak koordinatasini normallashtirilgan (0..1) ga qaytaradi. */
    fun toNormalized(point: Offset): Offset = Offset(x = point.x / width, y = point.y / height)

    /**
     * [gridPoint] dagi konturgacha ishorali masofa (kataklarda): ichkarida musbat,
     * tashqarida manfiy. Kataklar orasida bilinear interpolatsiya — chiziq katak
     * aniqligiga "sakramaydi" va qiymat silliq o'sadi.
     */
    fun distanceAt(gridPoint: Offset): Float {
        val x = gridPoint.x - 0.5f
        val y = gridPoint.y - 0.5f
        val x0 = kotlin.math.floor(x).toInt()
        val y0 = kotlin.math.floor(y).toInt()
        val tx = x - x0
        val ty = y - y0

        val top = lerp(at(x0, y0), at(x0 + 1, y0), tx)
        val bottom = lerp(at(x0, y0 + 1), at(x0 + 1, y0 + 1), tx)
        return lerp(top, bottom, ty)
    }

    /** Grid chetidan tashqarida — eng yaqin chekka katakning qiymati. */
    private fun at(x: Int, y: Int): Float =
        distance[y.coerceIn(0, height - 1) * width + x.coerceIn(0, width - 1)]

    private fun lerp(from: Float, to: Float, fraction: Float): Float = from + (to - from) * fraction
}

/**
 * Ichkarida musbat, tashqarida manfiy masofa: shakl ichidan chetgacha va shakl tashqarisidan
 * shaklgacha — ikkita hisob bitta xaritaga qo'shiladi.
 */
private fun signedDistance(width: Int, height: Int, inside: BooleanArray): FloatArray {
    // Ichkari uchun grid chekkasidan tashqarisi chet hisoblanadi: belgi bounding box'iga
    // tiqilib turgani uchun kontur aynan chekkada yotadi.
    val toEdge = distanceToBackground(width, height, inside, borderIsBackground = true)
    val toShape = distanceToBackground(width, height, BooleanArray(inside.size) { !inside[it] }, borderIsBackground = false)

    return FloatArray(inside.size) { index ->
        if (inside[index]) toEdge[index] else -toShape[index]
    }
}

/**
 * Har bir "foreground" katak uchun eng yaqin "background" katakgacha masofani hisoblaydi
 * (8SSEDT: ikkita o'tishda har katakka eng yaqin chet nuqtasining vektori tarqatiladi).
 */
private fun distanceToBackground(
    width: Int,
    height: Int,
    foreground: BooleanArray,
    borderIsBackground: Boolean,
): FloatArray {
    val inside = foreground
    val size = width * height
    // dx/dy — katakdan eng yaqin chet katagigacha vektor.
    val dx = IntArray(size)
    val dy = IntArray(size)
    val squared = FloatArray(size)

    for (index in 0 until size) {
        if (inside[index]) {
            squared[index] = FAR
        } else {
            squared[index] = 0f
            dx[index] = 0
            dy[index] = 0
        }
    }

    fun compare(index: Int, x: Int, y: Int, offsetX: Int, offsetY: Int) {
        val neighborX = x + offsetX
        val neighborY = y + offsetY

        val candidateX: Int
        val candidateY: Int
        if (neighborX < 0 || neighborY < 0 || neighborX >= width || neighborY >= height) {
            // Grid tashqarisidagi qo'shni: chet deb hisoblansa — o'zi seed, aks holda e'tiborsiz.
            if (!borderIsBackground) return
            candidateX = offsetX
            candidateY = offsetY
        } else {
            val neighbor = neighborY * width + neighborX
            if (squared[neighbor] >= FAR) return
            candidateX = dx[neighbor] + offsetX
            candidateY = dy[neighbor] + offsetY
        }

        val candidate = (candidateX * candidateX + candidateY * candidateY).toFloat()
        if (candidate < squared[index]) {
            squared[index] = candidate
            dx[index] = candidateX
            dy[index] = candidateY
        }
    }

    for (y in 0 until height) {
        for (x in 0 until width) {
            val index = y * width + x
            if (squared[index] <= 0f) continue
            compare(index, x, y, -1, 0)
            compare(index, x, y, 0, -1)
            compare(index, x, y, -1, -1)
            compare(index, x, y, 1, -1)
        }
    }
    for (y in height - 1 downTo 0) {
        for (x in width - 1 downTo 0) {
            val index = y * width + x
            if (squared[index] <= 0f) continue
            compare(index, x, y, 1, 0)
            compare(index, x, y, 0, 1)
            compare(index, x, y, 1, 1)
            compare(index, x, y, -1, 1)
        }
    }

    return FloatArray(size) { index -> sqrt(min(squared[index], FAR)) }
}

/** Boshlang'ich "cheksiz" masofa — grid diagonalidan kattaroq bo'lsa yetarli. */
private const val FAR = 1e9f