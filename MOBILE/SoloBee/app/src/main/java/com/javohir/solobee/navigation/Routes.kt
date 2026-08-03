package com.javohir.solobee.navigation

import android.net.Uri

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.solobee
 * Description: Mashrutlar
 */
object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"

    const val PROFILE = "profile"


    const val CATEGORY_ID_ARG = "categoryId"
    const val CATEGORIES = "categories/{$CATEGORY_ID_ARG}"
    const val SUB_CATEGORY_ID_ARG = "subCategoryId"
    const val SUB_CATEGORY_NAME_ARG = "subCategoryName"
    const val TOPIC_ID_ARG = "topicId"

    const val TOPICS = "topics/{$SUB_CATEGORY_ID_ARG}/{$SUB_CATEGORY_NAME_ARG}"
    const val LEARN = "learn/{$TOPIC_ID_ARG}/{$SUB_CATEGORY_NAME_ARG}"
    const val WRITING = "writing/{$TOPIC_ID_ARG}/{$SUB_CATEGORY_NAME_ARG}"
    const val TRACE = "trace/{$TOPIC_ID_ARG}/{$SUB_CATEGORY_NAME_ARG}"
    const val WORD_HUNT = "wordhunt/{$TOPIC_ID_ARG}/{$SUB_CATEGORY_NAME_ARG}"
    const val PIC_QUEST = "picquest/{$TOPIC_ID_ARG}/{$SUB_CATEGORY_NAME_ARG}"

    fun categories(categoryId: String): String = "categories/$categoryId"
    fun topics(subCategoryId: String, subCategoryName: String): String =
        "topics/${Uri.encode(subCategoryId)}/${Uri.encode(subCategoryName)}"

    fun learn(topicId: String, subCategoryName: String): String =
        "learn/${Uri.encode(topicId)}/${Uri.encode(subCategoryName)}"

    fun writing(topicId: String, subCategoryName: String): String =
        "writing/${Uri.encode(topicId)}/${Uri.encode(subCategoryName)}"

    fun trace(topicId: String, subCategoryName: String): String =
        "trace/${Uri.encode(topicId)}/${Uri.encode(subCategoryName)}"

    fun wordHunt(topicId: String, subCategoryName: String): String =
        "wordhunt/${Uri.encode(topicId)}/${Uri.encode(subCategoryName)}"

    fun picQuest(topicId: String, subCategoryName: String): String =
        "picquest/${Uri.encode(topicId)}/${Uri.encode(subCategoryName)}"

}