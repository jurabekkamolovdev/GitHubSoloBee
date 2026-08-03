package com.javohir.feature.picquest

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.picquest
 * Description: PicQuestIntent: foydalanuvchi amallari (MVI/intent).
 */

import com.javohir.domain.model.ActivityOption

sealed class PicQuestIntent {
    object Refresh : PicQuestIntent()
    object PlayPromptAudio : PicQuestIntent()
    data class OptionClicked(val option: ActivityOption) : PicQuestIntent()
}
