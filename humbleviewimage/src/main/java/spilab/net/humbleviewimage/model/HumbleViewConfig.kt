package spilab.net.humbleviewimage.model

import android.content.Context

class HumbleViewConfig {

    companion object {

        const val TAG = "HumbleView"

        var debug = false

        var log = false

        fun installHTTPCache(applicationContext: Context) {
            HumbleViewExecutor.installHTTPCache(applicationContext)
        }
    }
}