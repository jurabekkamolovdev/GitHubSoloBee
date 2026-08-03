package com.javohir.feature.writing

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing
 * Description: WRITING activity rejimi (payload.mode).
 */
enum class WritingMode {
    SPELL,

    TRACE;

    companion object {
        fun from(raw: String?): WritingMode? = when (raw?.trim()?.lowercase()) {
            "spell" -> SPELL
            "trace" -> TRACE
            else -> null
        }
    }
}
