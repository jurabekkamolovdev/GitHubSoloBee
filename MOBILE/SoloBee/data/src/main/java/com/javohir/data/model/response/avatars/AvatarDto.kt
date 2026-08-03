package com.javohir.data.model.response.avatars

import com.google.gson.annotations.SerializedName

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.avatars
 * Description: Response model — boy va girl ro'yxatlari uchun bir xil element.
 */
data class AvatarDto(
    @SerializedName(value = "id")
    val id: String?,
    @SerializedName(value = "gender")
    val gender: String?,
    @SerializedName(value = "thumbnailUrl")
    val thumbnailUrl: String?,
    @SerializedName(value = "orderIndex")
    val orderIndex: Int?
)