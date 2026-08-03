package com.javohir.feature.wordHunt

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.wordhunt
 * Description: WordHuntIntent: foydalanuvchi amallari (MVI/intent).
 */

import com.javohir.domain.model.ActivityOption

sealed class WordHuntIntent {

    object Refresh : WordHuntIntent()
    data class OptionClicked(val option: ActivityOption) : WordHuntIntent()
    data class PlayOptionAudio(val url: String) : WordHuntIntent()
}
