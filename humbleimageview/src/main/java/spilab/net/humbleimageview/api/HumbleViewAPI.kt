package spilab.net.humbleimageview.api


class HumbleViewAPI {

    companion object {

        val DEFAULT_FADING_SPEED_MILLIS = 300L;

        var fadingSpeedMillis = DEFAULT_FADING_SPEED_MILLIS
        var debug = false
        var log = false
        val http = Http()
        val cache = Cache()

        internal const val HTTP_CACHE_DIRECTORY = "humbleviewhttpcache"
        internal const val CACHE_DIRECTORY = "humbleviewcache"
    }
}