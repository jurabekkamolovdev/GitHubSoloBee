package com.javohir.solobee.cashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.javohir.utils.AppLogger
import javax.inject.Inject

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.solobee
 * Description: Crashlytics implementation
 */
class CrashlyticsLogger @Inject constructor(): AppLogger {
    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun logError(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

}