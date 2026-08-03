package com.javohir.feature.writing.trace

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.javohir.feature.R
import com.javohir.ui.theme.SoloBeeColors
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: Belgini barmoq bilan chizish maydoni.
 *
 * Chizish jarayoni (qaysi stroke faol, barmoq izi) shu yerda yashaydi — ViewModel'ga
 * chiqmaydi. [char] o'zgarganda (A -> a) butun progress o'zi noldan boshlanadi.
 */
@Composable
fun TraceCanvas(
    char: Char,
    modifier: Modifier = Modifier,
    onCompleted: () -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val currentOnCompleted by rememberUpdatedState(newValue = onCompleted)

    // Har bir shrift faqat o'zi kuchli bo'lgan joyda ishlatiladi:
    //  - Nunito Black: qalin, uchlari yumaloq bosh harflar va sonlar — maketdagidek.
    //    Baloo2 ham qalin, lekin uning oyoqlari yon tomonga yoyilib, ichki burchagida
    //    tishi bor; maketda esa uchlar yumaloq qalam bilan chizilganday.
    //  - Comic Neue: tik, doiraviy bir qavatli 'a'/'g' (maketdagidek). Uning bosh
    //    'A' sida bezak do'mboqcha bor — shuning uchun bosh harflarga tegmaydi.
    val typeface = remember(char) {
        val fontRes = if (char.isLowerCase()) R.font.comic_neue_bold else R.font.nunito_black
        ResourcesCompat.getFont(context, fontRes)
    }
    val glyph = remember(char) { TraceGlyphs.forChar(char = char) }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val outline = remember(char, canvasSize, typeface) {
        val typefaceOrNull = typeface ?: return@remember null
        if (canvasSize.width == 0 || canvasSize.height == 0) return@remember null
        buildGlyphOutline(
            char = char,
            typeface = typefaceOrNull,
            size = Size(width = canvasSize.width.toFloat(), height = canvasSize.height.toFloat()),
            padding = with(density) { CANVAS_PADDING.toPx() },
        )
    }

    // Jadvaldagi taxminiy chiziq shrifdagi haqiqiy shakl bo'yicha to'g'rilanadi: a'zoning
    // aniq o'rtasiga suriladi, uchi kontur ichida tugaydi. Canvas o'lchamiga bog'liq emas.
    val field = remember(char, typeface) { typeface?.let { buildGlyphField(char = char, typeface = it) } }
    val guides: List<List<Offset>> = remember(glyph, field) {
        val strokes = glyph?.strokes ?: return@remember emptyList()
        strokes.map { stroke ->
            if (field == null || !stroke.snapToShape) stroke.points
            else GlyphSkeleton.snap(points = stroke.points, field = field)
        }
    }

    val strokes: List<List<Offset>> = remember(guides, outline) {
        if (outline == null) emptyList() else guides.map { points -> outline.map(points = points) }
    }

    // Raqamli doiracha o'z chizig'i bilan birga suriladi: chiziq to'g'rilanganda doiracha
    // taxminiy joyida qolsa, u chiziqning boshidan uzilib, konturga chiqib qoladi.
    val badges: List<Offset> = remember(glyph, guides) {
        val strokeList = glyph?.strokes ?: return@remember emptyList()
        strokeList.mapIndexed { index, stroke ->
            val correction = guides[index].first() - stroke.points.first()
            stroke.badgeAt + correction
        }
    }
    val checkpoints: List<List<Offset>> = remember(strokes) {
        strokes.map { points ->
            TraceValidator.sampleCheckpoints(points = points, count = CHECKPOINT_COUNT)
        }
    }

    var strokeIndex by remember(char) { mutableIntStateOf(0) }
    var nextCheckpoint by remember(char) { mutableIntStateOf(0) }
    // Yopiq halqa uchun: markaz atrofida yig'ilgan aylanish burchagi (radian) va oxirgi nuqta.
    var sweptAngle by remember(char) { mutableFloatStateOf(0f) }
    var lastSweepPoint by remember(char) { mutableStateOf<Offset?>(null) }
    val fingerTrail = remember(char) { mutableStateListOf<Offset>() }

    // Yo'riqnoma a'zoning o'rta chizig'ida yotadi, bola esa ko'rinadigan a'zoni chizadi —
    // 'O' ning qalin halqasida chetdan markazgacha bemalol yarim a'zo eni bo'ladi. Fiksa
    // 26/44dp chegara bunday harf uchun juda tor: barmoq halqa chetida boshlansa stroke
    // umuman boshlanmaydi, sal chayqalsa off-path bo'lib progress nolga tushadi. Shuning
    // uchun chegaralarni a'zo qalinligiga moslaymiz — ingichka harflar qat'iy qoladi.
    val limbHalfWidthPx: Float = remember(guides, field, outline) {
        val glyphField = field ?: return@remember 0f
        val glyphOutline = outline ?: return@remember 0f
        val maxHalfWidth = guides.flatten()
            .maxOfOrNull { point -> glyphField.distanceAt(glyphField.toGrid(point)) }
            ?: return@remember 0f
        maxHalfWidth / glyphField.width * glyphOutline.bounds.width
    }
    // Nazorat nuqtalari a'zoning o'rta chizig'ida yotadi, bola esa a'zoning istalgan yeridan —
    // ichki yoki tashqi chetidan — chizishi mumkin. Chetdan markazgacha masofa aynan
    // limbHalfWidth, shuning uchun radius undan KATTA bo'lishi shart (0.9x yetmaydi: chetdan
    // chizgan bola checkpoint'ni bosolmay qoladi). 1.3x — chet + ozgina hoshiya, lekin
    // checkpoint oralig'idan (halqa aylanasi / 9) kichik, shuning uchun sakrab ketmaydi.
    val checkpointRadius = maxOf(with(density) { CHECKPOINT_RADIUS.toPx() }, limbHalfWidthPx * 1.3f)
    val offPathTolerance = maxOf(with(density) { OFF_PATH_TOLERANCE.toPx() }, limbHalfWidthPx * 1.7f)

    // Har stroke yopiq halqami — bo'lsa markazi, aks holda null. Yopiq halqa checkpoint tartibi
    // bilan emas, markaz atrofidagi aylanish burchagi bilan tugallanadi (yo'nalishga bog'liq emas).
    val strokeCenters: List<Offset?> = remember(strokes, checkpointRadius) {
        strokes.map { guide ->
            if (TraceValidator.isClosedLoop(points = guide, radius = checkpointRadius)) {
                TraceValidator.centroid(points = guide)
            } else {
                null
            }
        }
    }

    Canvas(
        modifier = modifier
            .onSizeChanged { size -> canvasSize = size }
            .pointerInput(checkpoints) {
                if (checkpoints.isEmpty()) return@pointerInput

                fun resetStroke() {
                    nextCheckpoint = 0
                    sweptAngle = 0f
                    lastSweepPoint = null
                    fingerTrail.clear()
                }

                detectDragGestures(
                    onDragStart = { offset ->
                        resetStroke()
                        val active = checkpoints.getOrNull(strokeIndex) ?: return@detectDragGestures
                        // Chizish faqat stroke boshidan boshlanadi.
                        if ((offset - active.first()).getDistance() <= checkpointRadius) {
                            nextCheckpoint = 1
                            lastSweepPoint = offset
                            fingerTrail.add(offset)
                        }
                    },
                    onDrag = { change, _ ->
                        val active = checkpoints.getOrNull(strokeIndex) ?: return@detectDragGestures
                        if (nextCheckpoint == 0) return@detectDragGestures

                        val point = change.position
                        val guide = strokes[strokeIndex]
                        if (TraceValidator.distanceToPolyline(points = guide, point = point) > offPathTolerance) {
                            resetStroke()
                            return@detectDragGestures
                        }

                        fingerTrail.add(point)
                        // Yopiq halqa: markaz atrofida burilishni yig'amiz (yo'nalishga bog'liq emas).
                        strokeCenters.getOrNull(strokeIndex)?.let { center ->
                            lastSweepPoint?.let { previous ->
                                sweptAngle += TraceValidator.sweepDelta(center = center, from = previous, to = point)
                            }
                            lastSweepPoint = point
                        }
                        // Ochiq stroke: checkpoint'lar ketma-ket.
                        nextCheckpoint = TraceValidator.advance(
                            checkpoints = active,
                            nextIndex = nextCheckpoint,
                            point = point,
                            radius = checkpointRadius,
                        )
                    },
                    onDragEnd = {
                        val active = checkpoints.getOrNull(strokeIndex)
                        val loopCenter = strokeCenters.getOrNull(strokeIndex)
                        val done = if (loopCenter != null) {
                            kotlin.math.abs(sweptAngle) >= LOOP_MIN_SWEEP_RAD
                        } else {
                            active != null && TraceValidator.isStrokeComplete(
                                checkpoints = active,
                                nextIndex = nextCheckpoint,
                                radius = checkpointRadius,
                            )
                        }
                        resetStroke()
                        if (done) {
                            strokeIndex++
                            if (strokeIndex >= checkpoints.size) currentOnCompleted()
                        }
                    },
                    onDragCancel = { resetStroke() },
                )
            }
    ) {
        val glyphOutline = outline ?: return@Canvas

        drawPath(path = glyphOutline.path, color = SoloBeeColors.White)
        drawPath(
            path = glyphOutline.path,
            color = SoloBeeColors.Black,
            style = Stroke(width = OUTLINE_WIDTH.toPx()),
        )

        // Jadvalda hali yo'q belgi — konturni ko'rsatamiz, yo'riqnomasiz.
        if (glyph == null) return@Canvas

        strokes.forEachIndexed { index, points ->
            if (index < strokeIndex) {
                drawCompletedStroke(points = points)
            } else {
                drawGuideStroke(points = points)
                drawArrowHead(points = points)
                drawBadge(
                    textMeasurer = textMeasurer,
                    center = glyphOutline.map(point = badges[index]),
                    number = index + 1,
                    active = index == strokeIndex,
                )
            }
        }

        if (fingerTrail.size > 1) {
            drawCompletedStroke(points = fingerTrail)
        }
    }
}

private fun DrawScope.polylinePath(points: List<Offset>): Path = Path().apply {
    moveTo(points.first().x, points.first().y)
    points.drop(n = 1).forEach { point -> lineTo(point.x, point.y) }
}

/**
 * Butun stroke bitta Path sifatida chiziladi. Har bir kesmani alohida chizsak,
 * punktir naqshi har kesmada qaytadan boshlanadi — nuqtalar zich bo'lgani uchun
 * natija uzluksiz chiziq bo'lib ko'rinadi.
 */
private fun DrawScope.drawGuideStroke(points: List<Offset>) {
    if (points.size < 2) return
    drawPath(
        path = polylinePath(points = points),
        color = GuideColor,
        style = Stroke(
            width = GUIDE_WIDTH.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(DASH_ON.toPx(), DASH_OFF.toPx()),
            ),
        ),
    )
}

private fun DrawScope.drawCompletedStroke(points: List<Offset>) {
    if (points.size < 2) return
    drawPath(
        path = polylinePath(points = points),
        color = SoloBeeColors.ProgressActiveInner,
        style = Stroke(
            width = TRAIL_WIDTH.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        ),
    )
}

/** Stroke oxiridagi uchburchak — chizish yo'nalishini ko'rsatadi. */
private fun DrawScope.drawArrowHead(points: List<Offset>) {
    if (points.size < 2) return
    val tip = points.last()
    val length = ARROW_LENGTH.toPx()

    // Yo'nalish oxirgi kesmadan emas, oxirgi bo'lakdan olinadi: nuqtalar zich bo'lgani
    // uchun oxirgi kesma juda kalta va uning burchagi tasodifiy tebranadi.
    val previous = points.lastOrNull { point -> (tip - point).getDistance() >= length }
        ?: points.first()
    val angle = atan2(y = tip.y - previous.y, x = tip.x - previous.x)
    val spread = 0.5f

    listOf(angle + Math.PI.toFloat() - spread, angle + Math.PI.toFloat() + spread).forEach { wing ->
        drawLine(
            color = GuideColor,
            start = tip,
            end = Offset(x = tip.x + length * cos(wing), y = tip.y + length * sin(wing)),
            strokeWidth = GUIDE_WIDTH.toPx(),
            cap = StrokeCap.Round,
        )
    }
}

private fun DrawScope.drawBadge(
    textMeasurer: TextMeasurer,
    center: Offset,
    number: Int,
    active: Boolean,
) {
    val radius = BADGE_RADIUS.toPx()
    drawCircle(
        color = if (active) BadgeActiveColor else GuideColor,
        radius = radius,
        center = center,
    )

    val layout = textMeasurer.measure(
        text = number.toString(),
        style = TextStyle(
            color = SoloBeeColors.White,
            fontSize = BADGE_FONT_SIZE,
            fontFamily = FontFamily(Font(resId = R.font.baloo2_bold)),
        ),
    )
    drawText(
        textLayoutResult = layout,
        topLeft = Offset(
            x = center.x - layout.size.width / 2f,
            y = center.y - layout.size.height / 2f,
        ),
    )
}

private val GuideColor = Color(0xFF9AA3AF)
private val BadgeActiveColor = Color(0xFF4B5563)

private val CANVAS_PADDING = 24.dp
private val OUTLINE_WIDTH = 3.dp
private val GUIDE_WIDTH = 3.dp
private val TRAIL_WIDTH = 10.dp
private val DASH_ON = 9.dp
private val DASH_OFF = 9.dp
private val ARROW_LENGTH = 12.dp
private val BADGE_RADIUS = 14.dp
private val BADGE_FONT_SIZE = 16.sp

/** Bitta stroke bo'ylab nechta nazorat nuqtasi. */
private const val CHECKPOINT_COUNT = 10

/**
 * Yopiq halqa (O, o, 0) tugallangan hisoblanishi uchun markaz atrofida kamida shuncha
 * radian aylanish kerak (~300°). To'liq 360° emas — bola halqani deyarli yopsa yetarli.
 */
private const val LOOP_MIN_SWEEP_RAD = 5.236f

/** Nazorat nuqtasi radiusi — bolalar uchun ataylab kengroq. */
private val CHECKPOINT_RADIUS = 26.dp

/** Shu masofadan uzoqlashsa stroke qaytadan boshlanadi. */
private val OFF_PATH_TOLERANCE = 44.dp
