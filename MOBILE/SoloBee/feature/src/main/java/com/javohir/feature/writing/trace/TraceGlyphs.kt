package com.javohir.feature.writing.trace

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Chizish yo'riqnomasi — qaysi stroke qayerdan qayerga, qaysi tartibda.
 *
 * Koordinatalar belgi konturining bounding box'iga nisbatan (0..1, y pastga) va ATAYLAB
 * TAXMINIY: ular niyatni bildiradi, aniq joyni emas. Ekranda har chiziq [GlyphSkeleton]
 * orqali shrifdagi haqiqiy shaklga yopishtiriladi — a'zoning aniq o'rtasidan o'tadi va
 * uchi kontur ichida tugaydi. Shuning uchun bu yerda pikselni ovlash shart emas, va
 * shrift almashsa ham chiziqlar joyida qoladi.
 *
 * Yangi belgi qo'shish: shu yerga taxminiy chizmasini yozish kifoya.
 */
object TraceGlyphs {

    fun forChar(char: Char): TraceGlyph? = glyphs[char]

    /** Jadvalda yo'riqnomasi bor belgilar. */
    val supported: Set<Char> get() = glyphs.keys

    private val glyphs: Map<Char, TraceGlyph> = mapOf(
        'A' to TraceGlyph(
            strokes = listOf(
                // Cho'qqidan chap oyoqqa.
                TraceStroke(
                    points = listOf(Offset(x = 0.46f, y = 0.12f), Offset(x = 0.10f, y = 0.95f)),
                    badgeOverride = Offset(x = 0.44f, y = 0.07f),
                ),
                // O'ng oyoqqa. Dizaynda bu chiziq cho'qqidan emas, sezilarli pastroqdan
                // boshlanadi: shunda 2-raqam 1-chining tagida joylashadi va ikkala chiziq
                // cho'qqida qo'shilib ketmay, ajralib turadi.
                TraceStroke(
                    points = listOf(Offset(x = 0.56f, y = 0.26f), Offset(x = 0.90f, y = 0.95f)),
                    badgeOverride = Offset(x = 0.56f, y = 0.20f),
                ),
                // Ko'ndalang chiziq, chapdan o'ngga. Dizayndagidek kalta — faqat
                // yo'nalishni ko'rsatadi, butun enini bosib o'tmaydi.
                TraceStroke(
                    points = listOf(Offset(x = 0.30f, y = 0.62f), Offset(x = 0.66f, y = 0.62f)),
                    badgeOverride = Offset(x = 0.26f, y = 0.60f),
                ),
            ),
        ),
        'B' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.12f),
                // Yuqori qorincha: tik chiziqdan o'ngga, aylanib qaytadi.
                TraceStroke(
                    points = listOf(Offset(x = 0.14f, y = 0.07f)) +
                        arc(0.45f, 0.27f, 0.30f, 0.20f, 270f, 450f) +
                        listOf(Offset(x = 0.14f, y = 0.47f)),
                    badgeOverride = Offset(x = 0.30f, y = 0.04f),
                ),
                // Pastki qorincha — yuqorisidan kengroq. Raqami pastroqda: o'rtada yuqori
                // qorinchaning strelkasi tugaydi, ustiga tushsa ikkisi chalkashadi.
                TraceStroke(
                    points = listOf(Offset(x = 0.14f, y = 0.50f)) +
                        arc(0.45f, 0.72f, 0.34f, 0.22f, 270f, 450f) +
                        listOf(Offset(x = 0.14f, y = 0.94f)),
                    badgeOverride = Offset(x = 0.34f, y = 0.60f),
                ),
            ),
        ),
        'C' to TraceGlyph(
            strokes = listOf(
                // O'ng-yuqoridan soat strelkasiga teskari, o'ng-pastda tugaydi.
                TraceStroke(
                    points = arc(0.5f, 0.5f, 0.35f, 0.36f, 305f, 55f),
                    badgeOverride = Offset(x = 0.82f, y = 0.12f),
                ),
            ),
        ),
        'D' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.12f),
                TraceStroke(
                    points = listOf(Offset(x = 0.12f, y = 0.05f)) +
                        arc(0.5f, 0.5f, 0.34f, 0.43f, 270f, 450f) +
                        listOf(Offset(x = 0.12f, y = 0.95f)),
                    badgeOverride = Offset(x = 0.30f, y = 0.05f),
                ),
            ),
        ),
        'E' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.12f),
                bar(y = 0.09f, fromX = 0.12f, toX = 0.88f, badgeX = 0.32f),
                bar(y = 0.48f, fromX = 0.12f, toX = 0.70f, badgeX = 0.32f),
                bar(y = 0.91f, fromX = 0.12f, toX = 0.88f, badgeX = 0.32f),
            ),
        ),
        'F' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.12f),
                bar(y = 0.09f, fromX = 0.12f, toX = 0.88f, badgeX = 0.32f),
                bar(y = 0.49f, fromX = 0.12f, toX = 0.68f, badgeX = 0.32f),
            ),
        ),
        'G' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = arc(0.5f, 0.5f, 0.36f, 0.37f, 315f, 25f),
                    badgeOverride = Offset(x = 0.80f, y = 0.10f),
                ),
                // O'ngdagi ko'ndalang tishcha, o'ngdan chapga.
                TraceStroke(
                    points = listOf(Offset(x = 0.86f, y = 0.56f), Offset(x = 0.64f, y = 0.56f)),
                    badgeOverride = Offset(x = 0.94f, y = 0.56f),
                ),
            ),
        ),
        'H' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.11f),
                stem(x = 0.89f, badgeX = 0.95f),
                bar(y = 0.50f, fromX = 0.14f, toX = 0.86f, badgeX = 0.32f),
            ),
        ),
        'I' to TraceGlyph(
            strokes = listOf(
                // Baloo2 dagi 'I' — oddiy tik chiziq, serifsiz. Yuqori va pastki ko'ndalang
                // chiziqlar shu shrifda yo'q, shuning uchun yo'riqnoma ham bitta.
                stem(x = 0.5f, fromY = 0.05f, toY = 0.95f, badgeX = 0.5f),
            ),
        ),
        'J' to TraceGlyph(
            strokes = listOf(
                // Tik chiziq va pastdagi ilmoq — bitta harakat.
                TraceStroke(
                    points = listOf(Offset(x = 0.78f, y = 0.03f), Offset(x = 0.78f, y = 0.72f)) +
                        arc(0.44f, 0.72f, 0.34f, 0.18f, 0f, 180f),
                    badgeOverride = Offset(x = 0.86f, y = 0.05f),
                ),
            ),
        ),
        'K' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.12f),
                // Yuqori qo'l: o'ng-yuqoridan tik chiziq o'rtasiga.
                TraceStroke(
                    points = listOf(Offset(x = 0.80f, y = 0.12f), Offset(x = 0.28f, y = 0.50f)),
                    badgeOverride = Offset(x = 0.86f, y = 0.08f),
                ),
                // Pastki oyoq: o'rtadan o'ng-pastga.
                TraceStroke(
                    points = listOf(Offset(x = 0.30f, y = 0.52f), Offset(x = 0.82f, y = 0.92f)),
                    badgeOverride = Offset(x = 0.24f, y = 0.58f),
                ),
            ),
        ),
        'L' to TraceGlyph(
            strokes = listOf(
                // Pastga, keyin o'ngga — bitta harakat.
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.12f, y = 0.03f),
                        Offset(x = 0.12f, y = 0.88f),
                        Offset(x = 0.90f, y = 0.88f),
                    ),
                    badgeOverride = Offset(x = 0.06f, y = 0.05f),
                ),
            ),
        ),
        'M' to TraceGlyph(
            strokes = listOf(
                // To'rtta alohida harakat: tik chiziq, pastga qiya, yuqoriga qiya, tik chiziq.
                // Bitta uzun ilon-izi chiziq emas — bola har a'zoni alohida chizadi va har
                // biri to'g'ri chiziq bo'lib qoladi.
                stem(x = 0.13f, fromY = 0.05f, badgeX = 0.06f),
                TraceStroke(
                    points = listOf(Offset(x = 0.16f, y = 0.08f), Offset(x = 0.48f, y = 0.70f)),
                    badgeOverride = Offset(x = 0.22f, y = 0.10f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.50f, y = 0.70f), Offset(x = 0.83f, y = 0.08f)),
                    badgeOverride = Offset(x = 0.56f, y = 0.74f),
                ),
                stem(x = 0.86f, fromY = 0.08f, badgeX = 0.93f),
            ),
        ),
        'N' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.10f),
                // Qiya chiziq. Uchlari ataylab tik chiziqlarga tegmaydi — tugunga kirsa,
                // yo'nalish o'sha yerda buziladi.
                TraceStroke(
                    points = listOf(Offset(x = 0.20f, y = 0.14f), Offset(x = 0.78f, y = 0.84f)),
                    badgeOverride = Offset(x = 0.28f, y = 0.10f),
                ),
                stem(x = 0.86f, badgeX = 0.92f),
            ),
        ),
        'O' to TraceGlyph(
            strokes = listOf(
                // Yuqoridan boshlab soat strelkasiga teskari to'liq aylana. Markaz va radiuslar
                // Nunito Black 'O' maskasining a'zo o'rta chizig'iga o'lchab moslangan: tepa/past
                // nuqtada perpendikulyar tik bo'lgani uchun u yerdagi gorizontal joyni center()
                // tuzata olmaydi — markaz X aynan 0.50 bo'lishi shart.
                TraceStroke(
                    points = arc(0.50f, 0.50f, 0.37f, 0.39f, 270f, -90f),
                    badgeOverride = Offset(x = 0.50f, y = 0.06f),
                ),
            ),
        ),
        'P' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.13f),
                TraceStroke(
                    points = listOf(Offset(x = 0.13f, y = 0.09f)) +
                        arc(0.60f, 0.33f, 0.24f, 0.24f, 270f, 450f) +
                        listOf(Offset(x = 0.13f, y = 0.58f)),
                    badgeOverride = Offset(x = 0.32f, y = 0.09f),
                ),
            ),
        ),
        'Q' to TraceGlyph(
            strokes = listOf(
                // Halqa SNAP QILINMAYDI (snapToShape = false). Dumcha ulanish tugunida skelet
                // o'rta chizig'i dumcha tomon egiladi va snap halqani o'sha yerda botiradi;
                // bundan tashqari java.awt va Android maskalari tugun atrofida biroz farq
                // qilib, render qurilma bilan mos kelmasdi. Buning o'rniga halqa markazi
                // Nunito Black 'Q' maskasidan o'lchab qo'yilgan (teshik markazi y≈0.39 —
                // dumcha bounding box'ni pastga cho'zgani uchun halqa yuqorida yotadi).
                TraceStroke(
                    points = arc(0.50f, 0.39f, 0.365f, 0.31f, 270f, -90f),
                    badgeOverride = Offset(x = 0.50f, y = 0.02f),
                    snapToShape = false,
                ),
                // Pastki o'ngdagi dumcha.
                TraceStroke(
                    points = listOf(Offset(x = 0.66f, y = 0.78f), Offset(x = 0.90f, y = 0.92f)),
                    badgeOverride = Offset(x = 0.60f, y = 0.74f),
                ),
            ),
        ),
        'R' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.10f),
                TraceStroke(
                    points = listOf(Offset(x = 0.10f, y = 0.08f)) +
                        arc(0.52f, 0.30f, 0.22f, 0.22f, 270f, 450f) +
                        listOf(Offset(x = 0.10f, y = 0.53f)),
                    badgeOverride = Offset(x = 0.30f, y = 0.08f),
                ),
                // Pastki oyoq.
                TraceStroke(
                    points = listOf(Offset(x = 0.45f, y = 0.56f), Offset(x = 0.92f, y = 0.95f)),
                    badgeOverride = Offset(x = 0.38f, y = 0.60f),
                ),
            ),
        ),
        'S' to TraceGlyph(
            strokes = listOf(
                // Ikkita qarama-qarshi yoy — bitta uzluksiz harakat.
                TraceStroke(
                    points = arc(0.46f, 0.24f, 0.34f, 0.20f, 335f, 90f) +
                        arc(0.47f, 0.66f, 0.33f, 0.21f, 270f, 517f),
                    badgeOverride = Offset(x = 0.84f, y = 0.10f),
                ),
            ),
        ),
        'T' to TraceGlyph(
            strokes = listOf(
                bar(y = 0.04f, fromX = 0.06f, toX = 0.94f, badgeX = 0.12f),
                stem(x = 0.47f, fromY = 0.06f, toY = 0.96f, badgeX = 0.55f),
            ),
        ),
        'U' to TraceGlyph(
            strokes = listOf(
                // Pastga, tagidan aylanib, yana yuqoriga.
                TraceStroke(
                    points = listOf(Offset(x = 0.10f, y = 0.03f), Offset(x = 0.10f, y = 0.62f)) +
                        arc(0.48f, 0.62f, 0.38f, 0.26f, 180f, 0f) +
                        listOf(Offset(x = 0.86f, y = 0.03f)),
                    badgeOverride = Offset(x = 0.04f, y = 0.05f),
                ),
            ),
        ),
        'V' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.08f, y = 0.03f),
                        Offset(x = 0.50f, y = 0.90f),
                        Offset(x = 0.92f, y = 0.03f),
                    ),
                    badgeOverride = Offset(x = 0.03f, y = 0.05f),
                ),
            ),
        ),
        'W' to TraceGlyph(
            strokes = listOf(
                // 'M' dagidek to'rtta alohida to'g'ri chiziq — bitta uzun ilon-izi emas.
                TraceStroke(
                    points = listOf(Offset(x = 0.09f, y = 0.06f), Offset(x = 0.28f, y = 0.86f)),
                    badgeOverride = Offset(x = 0.04f, y = 0.06f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.30f, y = 0.86f), Offset(x = 0.47f, y = 0.30f)),
                    badgeOverride = Offset(x = 0.36f, y = 0.90f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.53f, y = 0.30f), Offset(x = 0.70f, y = 0.86f)),
                    badgeOverride = Offset(x = 0.53f, y = 0.24f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.72f, y = 0.86f), Offset(x = 0.91f, y = 0.06f)),
                    badgeOverride = Offset(x = 0.78f, y = 0.90f),
                ),
            ),
        ),
        'X' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(Offset(x = 0.09f, y = 0.03f), Offset(x = 0.91f, y = 0.97f)),
                    badgeOverride = Offset(x = 0.04f, y = 0.05f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.91f, y = 0.03f), Offset(x = 0.09f, y = 0.97f)),
                    badgeOverride = Offset(x = 0.96f, y = 0.05f),
                ),
            ),
        ),
        'Y' to TraceGlyph(
            strokes = listOf(
                // Chap qo'l va tik chiziq — bitta harakat.
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.10f, y = 0.03f),
                        Offset(x = 0.47f, y = 0.50f),
                        Offset(x = 0.47f, y = 0.95f),
                    ),
                    badgeOverride = Offset(x = 0.05f, y = 0.05f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.88f, y = 0.03f), Offset(x = 0.50f, y = 0.48f)),
                    badgeOverride = Offset(x = 0.93f, y = 0.05f),
                ),
            ),
        ),
        'Z' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.06f, y = 0.05f),
                        Offset(x = 0.86f, y = 0.05f),
                        Offset(x = 0.10f, y = 0.90f),
                        Offset(x = 0.92f, y = 0.90f),
                    ),
                    badgeOverride = Offset(x = 0.03f, y = 0.05f),
                ),
            ),
        ),
        'a' to TraceGlyph(
            strokes = listOf(
                // Kosacha: o'ng-yuqoridan soat strelkasiga teskari aylanib, pastki-o'ngda tugaydi.
                // O'ng yon — tik chiziqning o'zi, shuning uchun yoy to'liq aylana emas.
                TraceStroke(
                    points = arc(
                        centerX = 0.42f,
                        centerY = 0.48f,
                        radiusX = 0.33f,
                        radiusY = 0.38f,
                        startDeg = 315f,
                        endDeg = 20f,
                    ),
                    badgeOverride = Offset(x = 0.66f, y = 0.10f),
                ),
                // O'ng tomondagi tik chiziq, yuqoridan pastga.
                TraceStroke(
                    points = listOf(Offset(x = 0.79f, y = 0.10f), Offset(x = 0.82f, y = 0.88f)),
                    badgeOverride = Offset(x = 0.86f, y = 0.10f),
                ),
            ),
        ),
        'b' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.10f),
                // Qorincha: tik chiziqning o'rtasidan o'ngga aylanib, pastiga qaytadi.
                TraceStroke(
                    points = arc(0.45f, 0.62f, 0.34f, 0.25f, 180f, 505f),
                    badgeOverride = Offset(x = 0.10f, y = 0.55f),
                ),
            ),
        ),
        'c' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = arc(0.5f, 0.5f, 0.36f, 0.37f, 319f, 41f),
                    badgeOverride = Offset(x = 0.84f, y = 0.14f),
                ),
            ),
        ),
        'd' to TraceGlyph(
            strokes = listOf(
                // Avval qorincha, keyin o'ngdagi uzun tik chiziq.
                TraceStroke(
                    points = arc(0.505f, 0.61f, 0.345f, 0.27f, 330f, 30f),
                    badgeOverride = Offset(x = 0.80f, y = 0.42f),
                ),
                stem(x = 0.90f, badgeX = 0.96f),
            ),
        ),
        'e' to TraceGlyph(
            strokes = listOf(
                // Ko'ndalang chiziqdan boshlanib, soat strelkasiga teskari aylanadi.
                TraceStroke(
                    points = listOf(Offset(x = 0.15f, y = 0.48f)) +
                        arc(0.5f, 0.5f, 0.35f, 0.36f, 352f, 45f),
                    badgeOverride = Offset(x = 0.16f, y = 0.48f),
                ),
            ),
        ),
        'f' to TraceGlyph(
            strokes = listOf(
                // Yuqoridagi ilmoq, keyin pastga tik chiziq.
                TraceStroke(
                    points = arc(0.60f, 0.17f, 0.26f, 0.14f, 340f, 190f) +
                        listOf(Offset(x = 0.42f, y = 0.35f), Offset(x = 0.42f, y = 0.95f)),
                    badgeOverride = Offset(x = 0.90f, y = 0.08f),
                ),
                bar(y = 0.39f, fromX = 0.12f, toX = 0.72f, badgeX = 0.16f),
            ),
        ),
        'g' to TraceGlyph(
            strokes = listOf(
                // Kosacha to'liq halqa (SNAP QILINMAYDI — comic_neue 'g' maskasidan o'lchangan).
                // Oldin bowl o'ngда x=0.68 da ochiq tugab, stem x=0.83 da boshlanardi — ular
                // uzuq edi. Endi halqa o'ng devori stem bilan bir joyda (x≈0.86), stem uning
                // yuqori-o'ng chetидан boshlanadi.
                TraceStroke(
                    points = arc(0.485f, 0.36f, 0.375f, 0.26f, 270f, -90f),
                    badgeOverride = Offset(x = 0.485f, y = 0.03f),
                    snapToShape = false,
                ),
                // Tik stem (halqaning yuqori-o'ngидан) va pastga cho'zilgan ilmoq (descender).
                TraceStroke(
                    points = listOf(Offset(x = 0.86f, y = 0.14f), Offset(x = 0.86f, y = 0.80f)) +
                        arc(0.48f, 0.78f, 0.38f, 0.15f, 0f, 160f),
                    badgeOverride = Offset(x = 0.93f, y = 0.14f),
                    snapToShape = false,
                ),
            ),
        ),
        'h' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.11f),
                // Kamar: tik chiziqdan yuqoriga aylanib, o'ng oyoqqa tushadi.
                TraceStroke(
                    points = arc(0.52f, 0.70f, 0.37f, 0.26f, 190f, 360f) +
                        listOf(Offset(x = 0.89f, y = 0.95f)),
                    badgeOverride = Offset(x = 0.13f, y = 0.66f),
                ),
            ),
        ),
        'i' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.5f, fromY = 0.35f, toY = 0.95f, badgeX = 0.5f),
                // Nuqta — juda qisqa chiziq sifatida chiziladi.
                TraceStroke(
                    points = listOf(Offset(x = 0.5f, y = 0.05f), Offset(x = 0.5f, y = 0.13f)),
                    badgeOverride = Offset(x = 0.5f, y = 0.09f),
                ),
            ),
        ),
        'j' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(Offset(x = 0.65f, y = 0.32f), Offset(x = 0.65f, y = 0.80f)) +
                        arc(0.40f, 0.80f, 0.25f, 0.13f, 0f, 180f),
                    badgeOverride = Offset(x = 0.72f, y = 0.34f),
                ),
                // Nuqta.
                TraceStroke(
                    points = listOf(Offset(x = 0.70f, y = 0.03f), Offset(x = 0.70f, y = 0.11f)),
                    badgeOverride = Offset(x = 0.70f, y = 0.07f),
                ),
            ),
        ),
        'k' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.10f),
                TraceStroke(
                    points = listOf(Offset(x = 0.90f, y = 0.42f), Offset(x = 0.15f, y = 0.65f)),
                    badgeOverride = Offset(x = 0.95f, y = 0.40f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.25f, y = 0.68f), Offset(x = 0.95f, y = 0.95f)),
                    badgeOverride = Offset(x = 0.18f, y = 0.72f),
                ),
            ),
        ),
        'l' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.5f, badgeX = 0.5f),
            ),
        ),
        'm' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.10f, fromY = 0.20f, toY = 0.85f, badgeX = 0.05f),
                // Birinchi kamar va oyoq.
                TraceStroke(
                    points = arc(0.30f, 0.32f, 0.20f, 0.16f, 180f, 360f) +
                        listOf(Offset(x = 0.50f, y = 0.85f)),
                    badgeOverride = Offset(x = 0.17f, y = 0.28f),
                ),
                // Ikkinchi kamar va oyoq.
                TraceStroke(
                    points = arc(0.69f, 0.32f, 0.19f, 0.16f, 180f, 360f) +
                        listOf(Offset(x = 0.88f, y = 0.85f)),
                    badgeOverride = Offset(x = 0.56f, y = 0.28f),
                ),
            ),
        ),
        'n' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.10f, fromY = 0.20f, toY = 0.85f, badgeX = 0.05f),
                TraceStroke(
                    points = arc(0.46f, 0.32f, 0.36f, 0.18f, 180f, 360f) +
                        listOf(Offset(x = 0.82f, y = 0.85f)),
                    badgeOverride = Offset(x = 0.17f, y = 0.28f),
                ),
            ),
        ),
        'o' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = arc(0.48f, 0.50f, 0.36f, 0.38f, 270f, -90f),
                    badgeOverride = Offset(x = 0.48f, y = 0.08f),
                ),
            ),
        ),
        'p' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.12f, fromY = 0.18f, toY = 0.92f, badgeX = 0.06f),
                TraceStroke(
                    points = listOf(Offset(x = 0.12f, y = 0.20f)) +
                        arc(0.49f, 0.36f, 0.36f, 0.24f, 270f, 450f) +
                        listOf(Offset(x = 0.14f, y = 0.60f)),
                    badgeOverride = Offset(x = 0.32f, y = 0.12f),
                ),
            ),
        ),
        'q' to TraceGlyph(
            strokes = listOf(
                // Kosacha — 'a' dagidek, o'ng yon tik chiziqning o'zi.
                TraceStroke(
                    points = arc(0.44f, 0.345f, 0.34f, 0.255f, 300f, 60f),
                    badgeOverride = Offset(x = 0.60f, y = 0.08f),
                ),
                // Tik chiziq va pastdagi dumcha.
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.78f, y = 0.10f),
                        Offset(x = 0.78f, y = 0.90f),
                        Offset(x = 0.95f, y = 0.98f),
                    ),
                    badgeOverride = Offset(x = 0.86f, y = 0.10f),
                ),
            ),
        ),
        'r' to TraceGlyph(
            strokes = listOf(
                stem(x = 0.20f, fromY = 0.15f, toY = 0.85f, badgeX = 0.13f),
                // Yelka.
                TraceStroke(
                    points = arc(0.50f, 0.34f, 0.30f, 0.18f, 180f, 350f),
                    badgeOverride = Offset(x = 0.27f, y = 0.28f),
                ),
            ),
        ),
        's' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = arc(0.45f, 0.30f, 0.32f, 0.19f, 335f, 90f) +
                        arc(0.44f, 0.68f, 0.30f, 0.20f, 270f, 517f),
                    badgeOverride = Offset(x = 0.80f, y = 0.16f),
                ),
            ),
        ),
        't' to TraceGlyph(
            strokes = listOf(
                // Tik chiziq va pastdagi ilmoq.
                TraceStroke(
                    points = listOf(Offset(x = 0.38f, y = 0.03f), Offset(x = 0.38f, y = 0.78f)) +
                        arc(0.60f, 0.78f, 0.22f, 0.10f, 180f, 0f),
                    badgeOverride = Offset(x = 0.45f, y = 0.05f),
                ),
                bar(y = 0.30f, fromX = 0.08f, toX = 0.74f, badgeX = 0.12f),
            ),
        ),
        'u' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(Offset(x = 0.13f, y = 0.03f), Offset(x = 0.13f, y = 0.62f)) +
                        arc(0.46f, 0.62f, 0.33f, 0.25f, 180f, 0f),
                    badgeOverride = Offset(x = 0.07f, y = 0.05f),
                ),
                stem(x = 0.80f, fromY = 0.03f, toY = 0.86f, badgeX = 0.87f),
            ),
        ),
        'v' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.11f, y = 0.05f),
                        Offset(x = 0.49f, y = 0.85f),
                        Offset(x = 0.91f, y = 0.05f),
                    ),
                    badgeOverride = Offset(x = 0.05f, y = 0.07f),
                ),
            ),
        ),
        'w' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.07f, y = 0.05f),
                        Offset(x = 0.27f, y = 0.83f),
                        Offset(x = 0.47f, y = 0.22f),
                        Offset(x = 0.70f, y = 0.83f),
                        Offset(x = 0.93f, y = 0.05f),
                    ),
                    badgeOverride = Offset(x = 0.03f, y = 0.07f),
                ),
            ),
        ),
        'x' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(Offset(x = 0.06f, y = 0.04f), Offset(x = 0.94f, y = 0.96f)),
                    badgeOverride = Offset(x = 0.03f, y = 0.05f),
                ),
                TraceStroke(
                    points = listOf(Offset(x = 0.94f, y = 0.04f), Offset(x = 0.06f, y = 0.96f)),
                    badgeOverride = Offset(x = 0.97f, y = 0.05f),
                ),
            ),
        ),
        'y' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(Offset(x = 0.11f, y = 0.03f), Offset(x = 0.50f, y = 0.62f)),
                    badgeOverride = Offset(x = 0.05f, y = 0.05f),
                ),
                // O'ng qo'l va pastga cho'zilgan ilmoq.
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.85f, y = 0.03f),
                        Offset(x = 0.42f, y = 0.80f),
                        Offset(x = 0.25f, y = 0.90f),
                        Offset(x = 0.10f, y = 0.85f),
                    ),
                    badgeOverride = Offset(x = 0.91f, y = 0.05f),
                ),
            ),
        ),
        'z' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.07f, y = 0.05f),
                        Offset(x = 0.78f, y = 0.05f),
                        Offset(x = 0.12f, y = 0.88f),
                        Offset(x = 0.90f, y = 0.88f),
                    ),
                    badgeOverride = Offset(x = 0.03f, y = 0.06f),
                ),
            ),
        ),
        '0' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = arc(0.47f, 0.50f, 0.36f, 0.41f, 270f, -90f),
                    badgeOverride = Offset(x = 0.47f, y = 0.06f),
                ),
            ),
        ),
        '1' to TraceGlyph(
            strokes = listOf(
                // Bayroqcha va tik chiziq.
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.09f, y = 0.25f),
                        Offset(x = 0.45f, y = 0.05f),
                        Offset(x = 0.45f, y = 0.90f),
                    ),
                    badgeOverride = Offset(x = 0.06f, y = 0.26f),
                ),
                bar(y = 0.92f, fromX = 0.16f, toX = 0.80f, badgeX = 0.12f),
            ),
        ),
        '3' to TraceGlyph(
            strokes = listOf(
                // Ikki kamar — bitta uzluksiz harakat. SNAP QILINMAYDI: arc'lar Nunito Black
                // '3' o'rta chizig'iga o'lchab moslangan. Snap qilinsa yuqori kamar shrift
                // shaklidan chiqib qolib, trimToInside uni kesar edi — yuqori yarmi yo'qolardi.
                TraceStroke(
                    points = arc(0.44f, 0.31f, 0.33f, 0.18f, 215f, 432f) +
                        arc(0.46f, 0.68f, 0.34f, 0.18f, 270f, 505f),
                    badgeOverride = Offset(x = 0.12f, y = 0.15f),
                    snapToShape = false,
                ),
            ),
        ),
        '4' to TraceGlyph(
            strokes = listOf(
                // Diagonal va ko'ndalang chiziq.
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.66f, y = 0.10f),
                        Offset(x = 0.12f, y = 0.68f),
                        Offset(x = 0.90f, y = 0.68f),
                    ),
                    badgeOverride = Offset(x = 0.70f, y = 0.10f),
                ),
                stem(x = 0.68f, fromY = 0.03f, toY = 0.90f, badgeX = 0.76f),
            ),
        ),
        '5' to TraceGlyph(
            strokes = listOf(
                // Tik chiziq va qorincha.
                TraceStroke(
                    points = listOf(Offset(x = 0.20f, y = 0.06f), Offset(x = 0.20f, y = 0.44f)) +
                        arc(0.45f, 0.67f, 0.35f, 0.23f, 270f, 520f),
                    badgeOverride = Offset(x = 0.14f, y = 0.07f),
                ),
                bar(y = 0.04f, fromX = 0.22f, toX = 0.86f, badgeX = 0.56f),
            ),
        ),
        '6' to TraceGlyph(
            strokes = listOf(
                // Yuqoridan kelib, pastda halqa yasaydi.
                TraceStroke(
                    points = arc(0.47f, 0.47f, 0.34f, 0.40f, 320f, 180f) +
                        arc(0.47f, 0.68f, 0.33f, 0.22f, 180f, -180f),
                    badgeOverride = Offset(x = 0.76f, y = 0.14f),
                ),
            ),
        ),
        '7' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = listOf(
                        Offset(x = 0.07f, y = 0.04f),
                        Offset(x = 0.80f, y = 0.04f),
                        Offset(x = 0.14f, y = 0.94f),
                    ),
                    badgeOverride = Offset(x = 0.04f, y = 0.05f),
                ),
            ),
        ),
        '8' to TraceGlyph(
            strokes = listOf(
                TraceStroke(
                    points = arc(0.47f, 0.27f, 0.30f, 0.22f, 270f, -90f),
                    badgeOverride = Offset(x = 0.47f, y = 0.06f),
                ),
                TraceStroke(
                    points = arc(0.47f, 0.72f, 0.36f, 0.26f, 270f, -90f),
                    badgeOverride = Offset(x = 0.47f, y = 0.46f),
                ),
            ),
        ),
        '9' to TraceGlyph(
            strokes = listOf(
                // Kosacha to'liq halqa (SNAP QILINMAYDI — maskadan o'lchangan: teshik markazi
                // (0.47, 0.32)). Oldin bowl o'ngда x=0.60 da ochiq tugab, stem x=0.75 da
                // boshlanardi — ikkisi bir-biriga ulanmasdi. Endi halqa o'ng devori stem bilan
                // bir joyda (x≈0.76) va stem uning yuqori-o'ng chetidan boshlanadi.
                TraceStroke(
                    points = arc(0.46f, 0.31f, 0.30f, 0.21f, 270f, -90f),
                    badgeOverride = Offset(x = 0.46f, y = 0.03f),
                    snapToShape = false,
                ),
                // Tik stem (halqaning yuqori-o'ngидан) va pastdagi ilmoq.
                TraceStroke(
                    points = listOf(Offset(x = 0.78f, y = 0.18f), Offset(x = 0.79f, y = 0.70f)) +
                        arc(0.46f, 0.72f, 0.33f, 0.18f, 0f, 165f),
                    badgeOverride = Offset(x = 0.87f, y = 0.18f),
                    snapToShape = false,
                ),
            ),
        ),
        '2' to TraceGlyph(
            strokes = listOf(
                // Bitta uzluksiz harakat: yuqori yoy -> diagonal -> asos.
                TraceStroke(
                    points = arc(
                        centerX = 0.47f,
                        centerY = 0.28f,
                        radiusX = 0.36f,
                        radiusY = 0.215f,
                        startDeg = 195f,
                        endDeg = 375f,
                    ) + listOf(
                        Offset(x = 0.14f, y = 0.94f),
                        Offset(x = 0.90f, y = 0.94f),
                    ),
                    badgeOverride = Offset(x = 0.09f, y = 0.20f),
                ),
            ),
        ),
    )

    /** Tik chiziq: yuqoridan pastga. */
    private fun stem(
        x: Float,
        fromY: Float = 0.03f,
        toY: Float = 0.97f,
        badgeX: Float = x - 0.06f,
    ): TraceStroke = TraceStroke(
        points = listOf(Offset(x = x, y = fromY), Offset(x = x, y = toY)),
        badgeOverride = Offset(x = badgeX, y = fromY + 0.02f),
    )

    /** Ko'ndalang chiziq: chapdan o'ngga. */
    private fun bar(
        y: Float,
        fromX: Float,
        toX: Float,
        badgeX: Float,
    ): TraceStroke = TraceStroke(
        points = listOf(Offset(x = fromX, y = y), Offset(x = toX, y = y)),
        badgeOverride = Offset(x = badgeX, y = y),
    )

    /**
     * Ellips yoyini siniq chiziqqa aylantiradi. Burchak gradusda, y pastga qaragani uchun
     * 270° — yuqori nuqta. [endDeg] < [startDeg] bo'lsa yoy teskari yo'nalishda boradi.
     */
    private fun arc(
        centerX: Float,
        centerY: Float,
        radiusX: Float,
        radiusY: Float,
        startDeg: Float,
        endDeg: Float,
        steps: Int = 24,
    ): List<Offset> = (0..steps).map { step ->
        val deg = startDeg + (endDeg - startDeg) * step / steps
        val rad = deg * PI.toFloat() / 180f
        Offset(
            x = centerX + radiusX * cos(rad),
            y = centerY + radiusY * sin(rad),
        )
    }
}
