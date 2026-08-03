package com.javohir.feature.writing.trace

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Yo'riqnoma chizig'ining shaklga yopishishi.
 *
 * Shrift konturi o'rniga qo'lda yasalgan sodda shakl ishlatiladi — [GlyphField] Android'ga
 * bog'liq emas, faqat maska oladi. Shu sababli o'rtaga surish, ichkariga tortish va uchni
 * kesish mantiqini shrifsiz, aniq kutilgan raqamlar bilan tekshirsa bo'ladi.
 */
class GlyphSkeletonTest {

    /** [left]..[right] x [top]..[bottom] to'rtburchak (katakda) to'ldirilgan 100x100 maydon. */
    private fun rectangleField(left: Int, right: Int, top: Int, bottom: Int): GlyphField {
        val inside = BooleanArray(SIDE * SIDE) { index ->
            val x = index % SIDE
            val y = index / SIDE
            x in left until right && y in top until bottom
        }
        return GlyphField(width = SIDE, height = SIDE, inside = inside)
    }

    @Test
    fun `off-center guide snaps onto the middle of the limb`() {
        // Tik ustun: x 40..60, ya'ni o'rtasi x = 50 (normallashtirilgan 0.5).
        val field = rectangleField(left = 40, right = 60, top = 10, bottom = 90)

        // Taxminiy chiziq ataylab chapga surilgan — chetiga yaqin.
        val snapped = GlyphSkeleton.snap(
            points = listOf(Offset(x = 0.44f, y = 0.20f), Offset(x = 0.44f, y = 0.80f)),
            field = field,
        )

        snapped.forEach { point ->
            assertEquals("Chiziq ustun o'rtasida emas: $point", 0.5f, point.x, 0.02f)
        }
    }

    @Test
    fun `guide drifting outside the shape is pulled back in`() {
        val field = rectangleField(left = 40, right = 60, top = 10, bottom = 90)

        // Chiziq shakldan butunlay tashqarida boshlanadi.
        val snapped = GlyphSkeleton.snap(
            points = listOf(Offset(x = 0.30f, y = 0.20f), Offset(x = 0.30f, y = 0.80f)),
            field = field,
        )

        snapped.forEach { point ->
            assertTrue("Nuqta shakldan tashqarida: $point", field.distanceAt(field.toGrid(point)) > 0f)
        }
    }

    @Test
    fun `guide ends inside the shape so the arrow head fits`() {
        val field = rectangleField(left = 40, right = 60, top = 10, bottom = 90)

        // Uchlari shakl chetidan oshib ketgan chiziq.
        val snapped = GlyphSkeleton.snap(
            points = listOf(Offset(x = 0.50f, y = 0.02f), Offset(x = 0.50f, y = 0.98f)),
            field = field,
        )

        listOf(snapped.first(), snapped.last()).forEach { end ->
            val distance = field.distanceAt(field.toGrid(end))
            assertTrue("Chiziq uchi konturga juda yaqin: $end (masofa $distance)", distance >= 3f)
        }
    }

    /**
     * Taxminiy chiziq a'zoning yo'nalishiga ham, joyiga ham mos kelmasa: bosh harflarda
     * ('B' qorinchasi) aynan shu hol bo'ladi — taxmin qo'pol, lekin chiziq baribir a'zo
     * o'rtasiga tushishi kerak.
     */
    @Test
    fun `a coarse guess at the wrong angle still finds the middle of the limb`() {
        // Ko'ndalang a'zo: y 40..60, o'rtasi y = 50.
        val field = rectangleField(left = 10, right = 90, top = 40, bottom = 60)

        // Taxmin qiyshiq: a'zo ko'ndalang, chiziq esa qiya va yuqoriroqda.
        val snapped = GlyphSkeleton.snap(
            points = listOf(Offset(x = 0.30f, y = 0.42f), Offset(x = 0.70f, y = 0.50f)),
            field = field,
        )

        snapped.forEach { point ->
            assertEquals("Chiziq a'zo o'rtasida emas: $point", 0.5f, point.y, 0.03f)
        }
    }

    /**
     * 'A' ning ko'ndalang chizig'i: ingichka a'zo semiz tugunga ulanadi. Chiziq shu tugunga
     * tortilib qiyshaymasligi va uchini kesish butun a'zoni yeb yubormasligi kerak.
     */
    @Test
    fun `a thin limb joined to a thick node survives and stays straight`() {
        val field = GlyphField(
            width = SIDE,
            height = SIDE,
            inside = BooleanArray(SIDE * SIDE) { index ->
                val x = index % SIDE
                val y = index / SIDE
                val thinBar = y in 46 until 54 && x in 20 until 80
                val thickNode = x in 70 until 95 && y in 20 until 80
                thinBar || thickNode
            },
        )

        val snapped = GlyphSkeleton.snap(
            points = listOf(Offset(x = 0.25f, y = 0.48f), Offset(x = 0.78f, y = 0.48f)),
            field = field,
        )

        // A'zoning katta qismi saqlanishi kerak — qoldiqqa aylanmasligi.
        val length = (snapped.last() - snapped.first()).getDistance()
        assertTrue("Chiziq qoldiqqa aylandi: uzunligi $length", length > 0.35f)

        // Va tugunga qarab yuqoriga qiyshaymasligi.
        snapped.forEach { point ->
            assertEquals("Chiziq tugunga tortilib qiyshaydi: $point", 0.5f, point.y, 0.03f)
        }
    }

    /**
     * Maketda 'A' oyoqlari ideal to'g'ri. Haqiqiy shrifda esa a'zoning o'rta chizig'i to'g'ri
     * emas — bu yerda oyoqning tubi maketdagidek yumaloq qilingan. Chiziq shu yumaloqlikni
     * takrorlab to'lqinlanmasligi kerak.
     */
    @Test
    fun `a straight stroke stays perfectly straight despite a wavy medial axis`() {
        val field = GlyphField(
            width = SIDE,
            height = SIDE,
            inside = BooleanArray(SIDE * SIDE) { index ->
                val x = index % SIDE
                val y = index / SIDE
                // Tik ustun, tubi o'ngga kengaygan yumaloq oyoq bilan.
                val stem = x in 40 until 60 && y in 10 until 80
                val foot = (x - 60) * (x - 60) + (y - 80) * (y - 80) < 18 * 18
                stem || foot
            },
        )

        val snapped = GlyphSkeleton.snap(
            points = listOf(Offset(x = 0.46f, y = 0.15f), Offset(x = 0.46f, y = 0.85f)),
            field = field,
        )

        // Ikki nuqtali stroke — natija ham aynan to'g'ri chiziq bo'lishi shart.
        assertEquals("Natija to'g'ri chiziq emas", 2, snapped.size)

        // Va u ustunning o'rtasidan o'tishi kerak, oyoq tomonga og'ib ketmasdan.
        snapped.forEach { point ->
            assertEquals("Chiziq ustun o'rtasida emas: $point", 0.5f, point.x, 0.03f)
        }
    }

    @Test
    fun `a diagonal limb keeps its direction after snapping`() {
        // Diagonal a'zo: 'A' oyog'iga o'xshash, teng qalinlikda.
        val inside = BooleanArray(SIDE * SIDE) { index ->
            val x = index % SIDE
            val y = index / SIDE
            // |x - y| < 8 -> yuqori chapdan pastki o'ngga ketgan qalin chiziq.
            x - y in -8..8
        }
        val field = GlyphField(width = SIDE, height = SIDE, inside = inside)

        val snapped = GlyphSkeleton.snap(
            points = listOf(Offset(x = 0.24f, y = 0.20f), Offset(x = 0.84f, y = 0.80f)),
            field = field,
        )

        // A'zoning o'rta chizig'i — aynan x == y.
        snapped.forEach { point ->
            assertEquals("Chiziq diagonal o'rtasidan chetda: $point", point.y, point.x, 0.03f)
        }
    }

    private companion object {
        const val SIDE = 100
    }
}