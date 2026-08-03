package com.javohir.feature.writing

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.writing
 * Description: Intents
 */
sealed class WritingIntent {

    object PlayCharAudio : WritingIntent()

    /** Joriy belgi to'liq chizildi — keyingi qadamga o'tiladi. */
    object TraceFinished : WritingIntent()
    object Submit : WritingIntent()
}
