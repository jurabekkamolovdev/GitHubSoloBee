package com.javohir.domain.model

import com.javohir.domain.common.Resource

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: parallel yuklangan profil+boshqa payload uchun data klasslar.
 */
data class ProfileWithActivities(
    val profile: Resource<Profile>,
    val activities: Resource<List<Activity>>,
)

data class ProfileWithCategories(
    val profile: Resource<Profile>,
    val categories: Resource<List<Category>>,
)

data class ProfileWithTopics(
    val profile: Resource<Profile>,
    val topics: Resource<List<Topic>>,
)
