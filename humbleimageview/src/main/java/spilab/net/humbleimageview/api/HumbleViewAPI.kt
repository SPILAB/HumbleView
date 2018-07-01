package spilab.net.humbleimageview.api

import android.os.Handler
import android.os.Looper
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.log.HumbleLogs


class HumbleViewAPI {

    companion object {

        const val DEFAULT_FADING_SPEED_MILLIS = 300L

        var fadingSpeedMillis = DEFAULT_FADING_SPEED_MILLIS
        var debug = false
        var log
            get() = HumbleLogs.enable
            set(value) {
                HumbleLogs.enable = value
            }

        val executor = Executor()
        val http = Http()
        val offlineCache = OfflineCache(executor.executorService,
                AndroidHandler(Handler(Looper.getMainLooper())))

        internal const val HTTP_CACHE_DIRECTORY = "humbleviewhttpcache"
        internal const val CACHE_DIRECTORY = "humbleviewcache"
    }
}