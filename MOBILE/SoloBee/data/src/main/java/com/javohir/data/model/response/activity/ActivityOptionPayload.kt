package com.javohir.data.model.response.activity

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.activity
 * Description: ActivityOptionPayload: loyiha moduli uchun Kotlin manba.
 */

import com.google.gson.annotations.SerializedName

data class ActivityOptionPayload(
    @SerializedName(value = "char")
    val char: String? = null,
    @SerializedName(value = "text")
    val text: String? = null,
    @SerializedName(value = "isCorrect")
    val isCorrect: Boolean = false,
    @SerializedName(value = "imageUrl")
    val imageUrl: String,
    @SerializedName(value = "audioUrl")
    val audioUrl: String? = null,
)
