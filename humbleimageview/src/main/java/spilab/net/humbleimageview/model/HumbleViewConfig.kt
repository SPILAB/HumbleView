package spilab.net.humbleimageview.model

import android.content.Context

class HumbleViewConfig {

    companion object {

        var debug = false

        var log = false

        fun installHTTPCache(applicationContext: Context) {
            HumbleViewExecutor.installHTTPCache(applicationContext)
        }
    }
}