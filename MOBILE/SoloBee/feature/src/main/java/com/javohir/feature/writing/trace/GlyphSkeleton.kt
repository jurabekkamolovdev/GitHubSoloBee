package com.javohir.feature.writing.trace

import androidx.compose.ui.geometry.Offset

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Yo'riqnoma chizig'ini harf a'zosining o'rta chizig'iga yopishtirish.
 *
 * [TraceGlyphs] dagi koordinatalar taxminiy — ular "qaysi stroke qayerdan qayerga, qaysi
 * tartibda" degan niyatni bildiradi, xolos. Aniq joyni shrifdagi haqiqiy shakl belgilaydi,
 * shuning uchun bu yerda har nuqta [GlyphField] bo'yicha to'g'rilanadi:
 *
 *  1. Chiziq teng oraliqli nuqtalarga qayta bo'linadi.
 *  2. Har nuqta o'z yo'nalishiga perpendikulyar bo'ylab a'zo o'rtasiga ko'tariladi.
 *  3. Chiziq silliqlanadi — diskret katakdan qolgan mayda tishlar yo'qoladi.
 *  4. Uchlari harf ichiga tortiladi — strelka konturdan tashqariga chiqmaydi.
 *
 * Natijada chiziq shrift o'zgarsa ham a'zoning aniq o'rtasidan o'tadi.
 */
object GlyphSkeleton {

    /**
     * [points] — normallashtirilgan (0..1) taxminiy chiziq. Qaytadigan qiymat ham
     * normallashtirilgan.
     *
     * Ikki nuqtali stroke — to'g'ri chiziq bo'lishi kerak degani ([TraceGlyphs] dagi `stem`,
     * `bar` va 'A' oyoqlari), shuning uchun natija ham aynan to'g'ri chiziq bo'ladi. Ko'p
     * nuqtali stroke — yoy; u a'zoning o'rta chizig'ini egilishi bilan takrorlaydi.
     */
    fun snap(points: List<Offset>, field: GlyphField): List<Offset> {
        if (points.size < 2) return points

        val grid = resample(points = points.map(field::toGrid), count = SAMPLE_COUNT)
        val centered = grid.mapIndexed { index, point ->
            center(point = point, perpendicular = perpendicularAt(grid, index), field = field)
        }

        if (points.size == 2) return straighten(grid = grid, centered = centered, field = field)

        // Nuqtalar har xil masofaga surilgani uchun oraliqlari buzildi — qayta tenglashtiramiz.
        val even = resample(points = centered, count = SAMPLE_COUNT)
        val smoothed = smooth(points = even, passes = SMOOTH_PASSES)
        return trimToInside(points = smoothed, field = field).map(field::toNormalized)
    }

    /**
     * O'rtaga surilgan nuqtalarga to'g'ri chiziq moslab, uni [grid] belgilagan uzunlikda
     * qirqadi.
     *
     * Nega surilgan nuqtalarni to'g'ridan-to'g'ri ishlatmaymiz: harf a'zosining o'rta chizig'i
     * aslida to'g'ri emas — oyoq va cho'qqi yumaloq, ko'ndalang chiziq ulangan joy do'ppayadi.
     * Nuqtama-nuqta ergashsak chiziq shu qiyshiqliklarni takrorlab to'lqinlanadi. Maketda esa
     * chiziq ideal to'g'ri: u skeletni takrorlamaydi, uning yo'nalishini ifodalaydi.
     */
    private fun straighten(grid: List<Offset>, centered: List<Offset>, field: GlyphField): List<Offset> {
        // Moslash uchun faqat a'zo tanasi olinadi: chiziq uchlari tugunga yoki kontur
        // burchagiga tegib turadi va yo'nalishni buzadi.
        val body = centered.subList(
            (centered.size * FIT_MARGIN_FRACTION).toInt(),
            centered.size - (centered.size * FIT_MARGIN_FRACTION).toInt(),
        )
        val origin = body.reduce { sum, point -> sum + point } / body.size.toFloat()
        val direction = principalDirection(points = body, origin = origin)
            ?: return trimToInside(points = centered, field = field).map(field::toNormalized)

        // Uzunlik taxminiy chiziqdan olinadi — 'A' ning ko'ndalang chizig'i maketdagidek
        // kalta qolishi, o'ng oyoqqacha cho'zilib ketmasligi kerak.
        val from = projection(point = grid.first(), origin = origin, direction = direction)
        val to = projection(point = grid.last(), origin = origin, direction = direction)

        val start = walkInside(origin, direction, from = from, to = to, field = field)
        val end = walkInside(origin, direction, from = to, to = from, field = field)
        if (start == null || end == null) {
            return trimToInside(points = centered, field = field).map(field::toNormalized)
        }

        return listOf(origin + direction * start, origin + direction * end).map(field::toNormalized)
    }

    /**
     * Nuqtalar bulutining bosh yo'nalishi (eng katta cho'zilish o'qi) — kvadratik og'ishlar
     * yig'indisini eng kichik qiladigan chiziqning yo'nalishi.
     */
    private fun principalDirection(points: List<Offset>, origin: Offset): Offset? {
        var xx = 0f
        var xy = 0f
        var yy = 0f
        points.forEach { point ->
            val dx = point.x - origin.x
            val dy = point.y - origin.y
            xx += dx * dx
            xy += dx * dy
            yy += dy * dy
        }
        if (xx + yy <= 0f) return null

        val angle = 0.5f * kotlin.math.atan2(2f * xy, xx - yy)
        val direction = Offset(x = kotlin.math.cos(angle), y = kotlin.math.sin(angle))

        // O'q ishorasiz — uni strokening chizilish yo'nalishiga qaratamiz.
        val along = points.last() - points.first()
        return if (direction.x * along.x + direction.y * along.y < 0f) direction * -1f else direction
    }

    /** [point] ning chiziqdagi o'rni (origin dan boshlab, direction bo'ylab). */
    private fun projection(point: Offset, origin: Offset, direction: Offset): Float =
        (point.x - origin.x) * direction.x + (point.y - origin.y) * direction.y

    /**
     * [from] dan [to] tomon yurib, chiziq harf ichiga ishonchli kirgan birinchi o'rinni
     * qaytaradi — shu yerda strelka kontur ichida qoladi.
     */
    private fun walkInside(
        origin: Offset,
        direction: Offset,
        from: Float,
        to: Float,
        field: GlyphField,
    ): Float? {
        val inset = END_INSET_FRACTION * field.longestSide
        val step = if (to > from) SEARCH_STEP else -SEARCH_STEP
        var walked = from
        while (if (to > from) walked <= to else walked >= to) {
            if (field.distanceAt(origin + direction * walked) >= inset) return walked
            walked += step
        }
        return null
    }

    /** Chiziqni yoy uzunligi bo'yicha teng [count] nuqtaga bo'ladi. */
    private fun resample(points: List<Offset>, count: Int): List<Offset> {
        val lengths = points.zipWithNext { from, to -> (to - from).getDistance() }
        val total = lengths.sum()
        if (total <= 0f) return points

        val step = total / (count - 1)
        val result = ArrayList<Offset>(count)
        result += points.first()

        var segment = 0
        var walked = 0f
        for (index in 1 until count - 1) {
            val target = index * step
            while (segment < lengths.size - 1 && walked + lengths[segment] < target) {
                walked += lengths[segment]
                segment++
            }
            val fraction = if (lengths[segment] > 0f) (target - walked) / lengths[segment] else 0f
            result += points[segment] + (points[segment + 1] - points[segment]) * fraction
        }
        result += points.last()
        return result
    }

    /** Nuqtadagi yo'nalishga perpendikulyar birlik vektor. */
    private fun perpendicularAt(points: List<Offset>, index: Int): Offset {
        val before = points[(index - 1).coerceAtLeast(0)]
        val after = points[(index + 1).coerceAtMost(points.lastIndex)]
        val direction = after - before
        val length = direction.getDistance()
        if (length <= 0f) return Offset(x = 0f, y = 0f)
        return Offset(x = -direction.y / length, y = direction.x / length)
    }

    /**
     * Nuqtani perpendikulyar bo'ylab masofa eng katta bo'ladigan joyga — ya'ni a'zoning
     * o'rtasiga — ko'taradi. Qadam bo'yiga tor kelganda ikkiga bo'linadi, shu tariqa
     * eng yaqin cho'qqiga aniq tushadi.
     *
     * Bu qidiruv LOKAL: nuqta o'zi turgan joydan yuqoriga qarab yuradi va birinchi
     * cho'qqida to'xtaydi. Shuning uchun taxmin qanchalik qo'pol bo'lishidan qat'i nazar
     * (shrifdagi qorincha radiusi taxminiykidan boshqa bo'lsa ham) nuqta o'z a'zosining
     * o'rtasini topadi va qo'shni a'zoga sakrab ketmaydi.
     *
     * Masofa ishorali bo'lgani uchun harfdan chiqib ketgan nuqta ham manfiy tomondan
     * ko'tarilib ichkariga qaytadi. Siljish [MAX_SHIFT_FRACTION] bilan cheklangan — a'zolar
     * qo'shilgan semiz tugun chiziqni o'ziga tortib qiyshaytirmasligi uchun.
     */
    private fun center(point: Offset, perpendicular: Offset, field: GlyphField): Offset {
        if (perpendicular == Offset(x = 0f, y = 0f)) return point

        val maxShift = MAX_SHIFT_FRACTION * field.longestSide
        var current = point
        var best = field.distanceAt(current)
        var shift = 0f
        var step = ASCENT_STEP

        while (step >= MIN_ASCENT_STEP) {
            val candidates = listOf(shift + step, shift - step)
                .filter { candidate -> kotlin.math.abs(candidate) <= maxShift }
                .map { candidate -> candidate to field.distanceAt(point + perpendicular * candidate) }
                .filter { (_, distance) -> distance > best }
                .maxByOrNull { (_, distance) -> distance }

            if (candidates == null) {
                step /= 2f
            } else {
                shift = candidates.first
                best = candidates.second
                current = point + perpendicular * shift
            }
        }
        return current
    }

    /** Qo'shnilari bilan o'rtachalash — katak aniqligidan qolgan mayda tishlarni yo'qotadi. */
    private fun smooth(points: List<Offset>, passes: Int): List<Offset> {
        var current = points
        repeat(passes) {
            current = current.mapIndexed { index, point ->
                val before = current[(index - 1).coerceAtLeast(0)]
                val after = current[(index + 1).coerceAtMost(current.lastIndex)]
                (before + point * 2f + after) / 4f
            }
        }
        return current
    }

    /**
     * Chiziqning harf ichida ishonchli yotgan eng uzun bo'lagini qaytaradi: uchi konturdan
     * kamida [END_INSET_FRACTION] chetda tursin — shunda strelka harf ichida qoladi.
     *
     * Taxminiy chiziqning uchi konturdan qanchalik oshib ketgani oldindan noma'lum, shuning
     * uchun qat'iy chegara qo'ymaymiz. Ammo 'i' ning nuqtasi kabi butunlay ingichka a'zoda
     * bu shartni hech bir nuqta bajarmasligi mumkin — u holda talab shu a'zoning o'z
     * qalinligiga qarab yumshatiladi.
     */
    private fun trimToInside(points: List<Offset>, field: GlyphField): List<Offset> {
        val distances = points.map { point -> field.distanceAt(point) }
        val inset = minOf(
            END_INSET_FRACTION * field.longestSide,
            (distances.max()) * THIN_LIMB_INSET_FRACTION,
        )
        return longestRun(points = points, distances = distances, inset = inset) ?: points
    }

    /** [inset] shartini bajaradigan ketma-ket nuqtalarning eng uzun bo'lagi. */
    private fun longestRun(points: List<Offset>, distances: List<Float>, inset: Float): List<Offset>? {
        var bestStart = -1
        var bestEnd = -1
        var start = -1

        distances.forEachIndexed { index, distance ->
            if (distance >= inset) {
                if (start < 0) start = index
                if (index - start > bestEnd - bestStart) {
                    bestStart = start
                    bestEnd = index
                }
            } else {
                start = -1
            }
        }

        return if (bestEnd - bestStart >= 1) points.subList(bestStart, bestEnd + 1) else null
    }

    /** Bitta stroke nechta nuqtaga bo'linadi — punktir uchun yetarlicha zich. */
    private const val SAMPLE_COUNT = 64

    /** Qidiruv qadami, katakda. */
    private const val SEARCH_STEP = 0.5f

    /** To'g'ri chiziq moslashda har uchdan e'tiborga olinmaydigan ulush. */
    private const val FIT_MARGIN_FRACTION = 0.2f

    /** Ko'tarilishning boshlang'ich qadami, katakda — qo'pol taxminni tuzatishga yetadi. */
    private const val ASCENT_STEP = 4f

    /** Qadam shundan kichrayganda cho'qqi topilgan hisoblanadi. */
    private const val MIN_ASCENT_STEP = 0.125f

    /** Nuqta o'z joyidan siljiy oladigan eng katta masofa, belgi kattaligiga nisbatan. */
    private const val MAX_SHIFT_FRACTION = 0.12f

    private const val SMOOTH_PASSES = 2

    /**
     * Chiziq uchi konturdan shuncha ichkarida tugaydi (strelka sig'adigan masofa).
     * Eng ingichka a'zoning ('A' ning ko'ndalang chizig'i) yarim qalinligidan sezilarli
     * kichik bo'lishi shart — aks holda butun a'zo "chetda" deb kesib tashlanadi.
     */
    private const val END_INSET_FRACTION = 0.03f

    /** Ingichka a'zoda talab shu a'zoning eng qalin joyiga nisbatan yumshatiladi. */
    private const val THIN_LIMB_INSET_FRACTION = 0.6f
}