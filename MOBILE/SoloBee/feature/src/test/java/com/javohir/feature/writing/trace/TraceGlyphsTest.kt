package com.javohir.feature.writing.trace

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing.trace
 * Description: TraceGlyphs jadvalining invariantlari.
 *
 * Jadvaldagi koordinatalar ataylab taxminiy — chiziq harf a'zosining o'rtasida yotishini
 * shrift konturi hal qiladi ([GlyphSkeleton]), shuning uchun bu yerda faqat jadvalning
 * o'zi tekshiriladi: har stroke chizishga yaroqli va har bosh harfning kichigi bor.
 */

class TraceGlyphsTest {

    @Test
    fun `every stroke has at least two points`() {
        val failures = TraceGlyphs.supported.sorted().flatMap { char ->
            TraceGlyphs.forChar(char = char)!!.strokes.mapIndexedNotNull { index, stroke ->
                "'$char' stroke ${index + 1}: ${stroke.points.size} ta nuqta"
                    .takeIf { stroke.points.size < 2 }
            }
        }
        assertTrue(failures.joinToString(separator = "\n"), failures.isEmpty())
    }

    @Test
    fun `every guide coordinate stays within the normalized glyph box`() {
        val failures = mutableListOf<String>()
        TraceGlyphs.supported.sorted().forEach { char ->
            TraceGlyphs.forChar(char = char)!!.strokes.forEachIndexed { index, stroke ->
                stroke.points.forEach { point ->
                    if (point.x !in 0f..1f || point.y !in 0f..1f) {
                        failures += "'$char' stroke ${index + 1}: (${point.x}, ${point.y}) 0..1 dan tashqarida"
                    }
                }
            }
        }
        assertTrue(failures.joinToString(separator = "\n"), failures.isEmpty())
    }

    /** Bosh harf uchun kichigi ham bo'lishi shart — ekran A -> a ketma-ketligini chizadi. */
    @Test
    fun `letters are supported in both cases`() {
        val missing = TraceGlyphs.supported
            .filter { it.isLetter() }
            .flatMap { listOf(it.uppercaseChar(), it.lowercaseChar()) }
            .filterNot { it in TraceGlyphs.supported }
            .distinct()

        assertTrue("Juftsiz harflar: $missing", missing.isEmpty())
    }
}