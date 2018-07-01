package spilab.net.humbleimageview.common

import android.os.Handler
import spilab.net.humbleimageview.android.AndroidHandler

internal class AndroidHandlerMock(handler: Handler) : AndroidHandler(handler) {

    override fun post(runnable: Runnable): Boolean {
        runnable.run()
        return true
    }
}