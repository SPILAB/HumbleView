package spilab.net.humbleimageview.android

import android.os.Handler

internal open class AndroidHandler(private val handler: Handler) {

    open fun post(runnable: Runnable): Boolean {
        return handler.post(runnable)
    }
}