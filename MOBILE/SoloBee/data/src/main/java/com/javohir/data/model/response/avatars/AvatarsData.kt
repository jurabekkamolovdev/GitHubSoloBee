package com.javohir.data.model.response.avatars

import com.google.gson.annotations.SerializedName

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.avatars
 * Description: Response model
 */
data class AvatarsData(
    @SerializedName(value = "boy")
    val boy: List<AvatarDto>? = null,
    @SerializedName(value = "girl")
    val girl: List<AvatarDto>? = null
)