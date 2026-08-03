package com.javohir.feature.writing.trace

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: TraceValidator unit testlari.
 */
class TraceValidatorTest {

    private val horizontalLine = listOf(Offset(x = 0f, y = 0f), Offset(x = 100f, y = 0f))

    @Test
    fun `sampleCheckpoints spreads points evenly along a straight line`() {
        val checkpoints = TraceValidator.sampleCheckpoints(points = horizontalLine, count = 5)

        assertEquals(5, checkpoints.size)
        assertEquals(listOf(0f, 25f, 50f, 75f, 100f), checkpoints.map { it.x })
    }

    @Test
    fun `sampleCheckpoints keeps start and end of a multi segment polyline`() {
        val polyline = listOf(
            Offset(x = 0f, y = 0f),
            Offset(x = 0f, y = 60f),
            Offset(x = 40f, y = 60f),
        )

        val checkpoints = TraceValidator.sampleCheckpoints(points = polyline, count = 6)

        assertEquals(polyline.first(), checkpoints.first())
        assertEquals(polyline.last(), checkpoints.last())
    }

    @Test
    fun `sampleCheckpoints handles degenerate input`() {
        assertTrue(TraceValidator.sampleCheckpoints(points = emptyList(), count = 5).isEmpty())
        assertEquals(
            listOf(Offset(x = 3f, y = 3f)),
            TraceValidator.sampleCheckpoints(points = listOf(Offset(x = 3f, y = 3f)), count = 5),
        )
        // Uzunligi nol bo'lgan chiziq cheksiz siklga tushmasligi kerak.
        assertEquals(
            listOf(Offset.Zero),
            TraceValidator.sampleCheckpoints(
                points = listOf(Offset.Zero, Offset.Zero),
                count = 4,
            ),
        )
    }

    @Test
    fun `advance moves forward only when the next checkpoint is touched in order`() {
        val checkpoints = TraceValidator.sampleCheckpoints(points = horizontalLine, count = 5)

        // Uchinchi checkpoint'ga tegish navbatdagisini (1-indeks) o'tkazib yubormaydi.
        val skipped = TraceValidator.advance(
            checkpoints = checkpoints,
            nextIndex = 1,
            point = Offset(x = 50f, y = 0f),
            radius = 10f,
        )
        assertEquals(1, skipped)

        val advanced = TraceValidator.advance(
            checkpoints = checkpoints,
            nextIndex = 1,
            point = Offset(x = 25f, y = 0f),
            radius = 10f,
        )
        assertEquals(2, advanced)
    }

    @Test
    fun `advance consumes several checkpoints when the radius covers them`() {
        val checkpoints = TraceValidator.sampleCheckpoints(points = horizontalLine, count = 5)

        val advanced = TraceValidator.advance(
            checkpoints = checkpoints,
            nextIndex = 0,
            point = Offset(x = 25f, y = 0f),
            radius = 30f,
        )

        // 0, 25 va 50 radius ichida; 75 emas.
        assertEquals(3, advanced)
    }

    @Test
    fun `advance never runs past the last checkpoint`() {
        val checkpoints = TraceValidator.sampleCheckpoints(points = horizontalLine, count = 5)

        val advanced = TraceValidator.advance(
            checkpoints = checkpoints,
            nextIndex = 0,
            point = Offset(x = 50f, y = 0f),
            radius = 1_000f,
        )

        assertEquals(checkpoints.size, advanced)
    }

    @Test
    fun `open stroke completes only when the very last checkpoint is reached`() {
        val checkpoints = TraceValidator.sampleCheckpoints(points = horizontalLine, count = 5)

        assertTrue(TraceValidator.isStrokeComplete(checkpoints, nextIndex = 5, radius = 10f))
        assertTrue(!TraceValidator.isStrokeComplete(checkpoints, nextIndex = 4, radius = 10f))
    }

    private val square = listOf(
        Offset(x = 50f, y = 0f),
        Offset(x = 100f, y = 50f),
        Offset(x = 50f, y = 100f),
        Offset(x = 0f, y = 50f),
        Offset(x = 50f, y = 0f),
    )

    @Test
    fun `isClosedLoop detects a stroke whose start and end coincide`() {
        assertTrue(TraceValidator.isClosedLoop(points = square, radius = 26f))
        assertTrue(!TraceValidator.isClosedLoop(points = horizontalLine, radius = 26f))
    }

    @Test
    fun `centroid is the average of the points`() {
        val center = TraceValidator.centroid(points = listOf(Offset(0f, 0f), Offset(10f, 0f), Offset(10f, 10f), Offset(0f, 10f)))
        assertEquals(5f, center.x, 0.001f)
        assertEquals(5f, center.y, 0.001f)
    }

    @Test
    fun `sweeping a full loop accumulates about 2 PI regardless of direction`() {
        val center = Offset(x = 50f, y = 50f)
        // 24 nuqtali aylana; ikkala yo'nalishda ham modul ~2PI bo'lishi kerak.
        val ring = (0..24).map { step ->
            val a = 2 * Math.PI * step / 24
            Offset(x = 50f + 40f * kotlin.math.cos(a).toFloat(), y = 50f + 40f * kotlin.math.sin(a).toFloat())
        }
        fun total(points: List<Offset>): Float =
            points.zipWithNext { a, b -> TraceValidator.sweepDelta(center, a, b) }.sum()

        assertEquals(2 * Math.PI.toFloat(), kotlin.math.abs(total(ring)), 0.01f)
        assertEquals(2 * Math.PI.toFloat(), kotlin.math.abs(total(ring.reversed())), 0.01f)
        // Yarim aylana — atigi ~PI, to'liq emas.
        assertEquals(Math.PI.toFloat(), kotlin.math.abs(total(ring.subList(0, 13))), 0.01f)
    }

    @Test
    fun `distanceToPolyline measures perpendicular distance to the nearest segment`() {
        assertEquals(
            8f,
            TraceValidator.distanceToPolyline(points = horizontalLine, point = Offset(x = 50f, y = 8f)),
            0.001f,
        )
    }

    @Test
    fun `distanceToPolyline clamps to segment ends`() {
        // Chiziq oxiridan o'ngga chiqib ketgan nuqta uchun eng yaqin joy — oxirgi nuqta.
        assertEquals(
            20f,
            TraceValidator.distanceToPolyline(points = horizontalLine, point = Offset(x = 120f, y = 0f)),
            0.001f,
        )
    }
}
