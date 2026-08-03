package com.javohir.feature.writing.trace

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.atan2

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Chizishni tekshirish — checkpoint'lar bo'ylab.
 *
 * Barmoq stroke bo'ylab joylashgan checkpoint'larni **tartib bilan** bosib o'tishi kerak.
 * Bolalar uchun ataylab kechirimli: aniq path ustidan yurish shart emas, checkpoint
 * radiusiga tegsa kifoya.
 */
object TraceValidator {

    /**
     * Siniq chiziqni uzunligi bo'yicha teng oraliqdagi [count] ta nuqtaga ajratadi.
     * Birinchi va oxirgi checkpoint har doim stroke'ning boshi va oxiri.
     */
    fun sampleCheckpoints(points: List<Offset>, count: Int): List<Offset> {
        if (points.isEmpty()) return emptyList()
        if (points.size == 1 || count <= 1) return listOf(points.first())

        val segmentLengths = points.zipWithNext { a, b -> (b - a).getDistance() }
        val totalLength = segmentLengths.sum()
        if (totalLength == 0f) return listOf(points.first())

        return (0 until count).map { index ->
            val target = totalLength * index / (count - 1)
            pointAtDistance(points = points, segmentLengths = segmentLengths, distance = target)
        }
    }

    /**
     * Barmoq [point] ga tegdi. Ketma-ket kelayotgan checkpoint'lardan qaysilari
     * [radius] ichiga tushsa, ularni bosib o'tilgan deb belgilaydi va yangi
     * "keyingi kutilayotgan" indeksni qaytaradi.
     *
     * Tez chizganda bitta harakat bir nechta checkpoint ustidan o'tishi mumkin,
     * shuning uchun sikl bilan oldinga suriladi.
     */
    fun advance(
        checkpoints: List<Offset>,
        nextIndex: Int,
        point: Offset,
        radius: Float,
    ): Int {
        var index = nextIndex
        while (index < checkpoints.size && (point - checkpoints[index]).getDistance() <= radius) {
            index++
        }
        return index
    }

    /**
     * Ochiq stroke tugadimi — barcha checkpoint ketma-ket bosilishi kerak.
     * Yopiq halqa ('O', 'o', '0') bu yo'l bilan tekshirilmaydi (pastdagi [isClosedLoop] +
     * [sweepDelta] ga qarang): checkpoint ketma-ketligi yo'nalishga bog'liq bo'lib qoladi va
     * bola halqani teskari (soat yo'nalishida) chizsa hech qachon o'tmasdi.
     */
    fun isStrokeComplete(checkpoints: List<Offset>, nextIndex: Int, radius: Float): Boolean =
        nextIndex >= checkpoints.size

    /**
     * Stroke yopiq halqami — boshi va oxiri [radius] ichida ustma-ust ('O', 'o', '0').
     * Bunday strokeni checkpoint tartibi bilan emas, markaz atrofida aylanish burchagi bilan
     * tekshiramiz — shunda ikkala yo'nalishda ham, halqaning istalgan yeridan ham o'tadi.
     */
    fun isClosedLoop(points: List<Offset>, radius: Float): Boolean =
        points.size >= 2 && (points.first() - points.last()).getDistance() <= radius

    /** Nuqtalar to'plamining og'irlik markazi — yopiq halqaning markazi. */
    fun centroid(points: List<Offset>): Offset {
        if (points.isEmpty()) return Offset.Zero
        var sumX = 0f
        var sumY = 0f
        points.forEach { point -> sumX += point.x; sumY += point.y }
        return Offset(x = sumX / points.size, y = sumY / points.size)
    }

    /**
     * [center] atrofida [from] dan [to] ga burilgan burchak (radian, [-PI..PI]). Barmoq
     * harakati bo'ylab yig'ib borilsa, to'liq aylana ±2PI beradi — ishorasi yo'nalishni
     * bildiradi, kattaligi qancha aylangani. Halqa tekshiruvi shu yig'indining moduliga qaraydi.
     */
    fun sweepDelta(center: Offset, from: Offset, to: Offset): Float {
        val start = atan2((from.y - center.y).toDouble(), (from.x - center.x).toDouble())
        val end = atan2((to.y - center.y).toDouble(), (to.x - center.x).toDouble())
        var delta = end - start
        while (delta > PI) delta -= 2 * PI
        while (delta < -PI) delta += 2 * PI
        return delta.toFloat()
    }

    /** Barmoq stroke chizig'idan qanchalik uzoqda. Tolerance tekshiruvi uchun. */
    fun distanceToPolyline(points: List<Offset>, point: Offset): Float {
        if (points.isEmpty()) return Float.MAX_VALUE
        if (points.size == 1) return (point - points.first()).getDistance()
        return points.zipWithNext { a, b -> distanceToSegment(point = point, start = a, end = b) }
            .min()
    }

    private fun pointAtDistance(
        points: List<Offset>,
        segmentLengths: List<Float>,
        distance: Float,
    ): Offset {
        var remaining = distance
        segmentLengths.forEachIndexed { index, length ->
            if (remaining <= length || index == segmentLengths.lastIndex) {
                val fraction = if (length == 0f) 0f else (remaining / length).coerceIn(0f, 1f)
                return lerp(start = points[index], end = points[index + 1], fraction = fraction)
            }
            remaining -= length
        }
        return points.last()
    }

    private fun lerp(start: Offset, end: Offset, fraction: Float): Offset = Offset(
        x = start.x + (end.x - start.x) * fraction,
        y = start.y + (end.y - start.y) * fraction,
    )

    private fun distanceToSegment(point: Offset, start: Offset, end: Offset): Float {
        val segment = end - start
        val lengthSquared = segment.x * segment.x + segment.y * segment.y
        if (lengthSquared == 0f) return (point - start).getDistance()

        val toPoint = point - start
        val projection = (toPoint.x * segment.x + toPoint.y * segment.y) / lengthSquared
        val clamped = projection.coerceIn(0f, 1f)
        val closest = Offset(x = start.x + segment.x * clamped, y = start.y + segment.y * clamped)
        return (point - closest).getDistance()
    }
}
