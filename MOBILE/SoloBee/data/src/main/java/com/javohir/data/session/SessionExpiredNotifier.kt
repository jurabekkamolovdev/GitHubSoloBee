package com.javohir.data.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Emits when refresh fails and local auth tokens are cleared.
 */
@Singleton
class SessionExpiredNotifier @Inject constructor() {

    private val _events = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val events: SharedFlow<Unit> = _events.asSharedFlow()

    fun notifySessionExpired() {
        _events.tryEmit(Unit)
    }
}
