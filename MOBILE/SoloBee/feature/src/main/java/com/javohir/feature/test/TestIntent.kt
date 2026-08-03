package com.javohir.feature.test

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.test
 * Description: User Actions
 */
sealed class TestIntent {

    object Refresh : TestIntent()
    object PlayWordAudio : TestIntent()
    object Submit : TestIntent()
    object Clear : TestIntent()
    data class SelectLetter(val index: Int) : TestIntent()
    data class RemoveLetter(val typedPosition: Int) : TestIntent()
}
