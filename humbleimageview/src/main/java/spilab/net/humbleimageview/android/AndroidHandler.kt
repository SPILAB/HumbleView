package spilab.net.humbleimageview.android

import android.os.Handler

internal open class AndroidHandler(private val handler: Handler) {

    open fun post(runnable: Runnable): Boolean {
        return handler.post(runnable)
    }

    open fun postDelayed(runnable: Runnable, delayMillis: Long): Boolean {
        return handler.postDelayed(runnable, delayMillis)
    }

    open fun removeCallbacks(runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }
}