package ua.frist008.action.record.core.util.concurrent

import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

@Suppress("TooGenericExceptionCaught")
suspend inline fun awaitCancellation(crossinline block: () -> Unit) {
    try {
        kotlinx.coroutines.awaitCancellation()
    } catch (_: CancellationException) {
        block()
        throw CancellationException()
    } catch (throwable: Throwable) {
        Timber.e(throwable)
    }
}
