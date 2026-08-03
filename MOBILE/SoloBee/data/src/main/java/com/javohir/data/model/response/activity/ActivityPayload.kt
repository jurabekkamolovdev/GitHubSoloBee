package com.javohir.data.model.response.activity

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.activity
 * Description: ActivityPayload: loyiha moduli uchun Kotlin manba.
 */

import com.google.gson.annotations.SerializedName

data class ActivityPayload(
    @SerializedName(value = "mode")
    val mode: String? = null,
    @SerializedName(value = "char")
    val char: String? = null,
    @SerializedName(value = "answer")
    val answer: String? = null,
    @SerializedName(value = "imageUrl")
    val imageUrl: String? = null,
    @SerializedName(value = "audioUrl")
    val audioUrl: String? = null,
    @SerializedName(value = "options")
    val options: List<ActivityOptionPayload>? = null,
)
