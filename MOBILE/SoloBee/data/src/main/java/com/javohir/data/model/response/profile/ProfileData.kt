package com.javohir.data.model.response.profile

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.model.response.profile
 * Description: ProfileData: API JSON mos keladigan data klasslari.
 */

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    // Yangi ro'yxatdan o'tgan foydalanuvchida hali ball yo'q — server null qaytaradi
    @SerializedName("score")
    val score: String? = null,
)
