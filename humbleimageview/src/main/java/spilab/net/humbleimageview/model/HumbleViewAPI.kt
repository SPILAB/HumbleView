package spilab.net.humbleimageview.model

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File


class HumbleViewAPI {

    companion object {

        var debug = false
        var log = false

        private var DEFAULT_CACHE_SIZE = 32L * 1024L * 1024L // 32 MiB
        private var cache: Cache? = null
        private var okHttpClient: OkHttpClient? = null

        internal fun getOkHttpClient(context: Context): OkHttpClient {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient()
                        .newBuilder()
                        .cache(getCache(context)).build()
            }
            return okHttpClient!!
        }

        private fun getCache(context: Context): Cache {
            if (cache == null) {
                cache = Cache(getDefaultCacheDir(context), DEFAULT_CACHE_SIZE)
            }
            return cache!!
        }

        private fun getDefaultCacheDir(context: Context): File {
            val cacheDir = File(context.cacheDir, "humbleviewhttpcache")
            cacheDir.mkdirs()
            return cacheDir
        }
    }
}