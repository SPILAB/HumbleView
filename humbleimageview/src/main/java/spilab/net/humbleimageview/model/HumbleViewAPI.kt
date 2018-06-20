package spilab.net.humbleimageview.model

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import android.os.AsyncTask


class HumbleViewAPI {

    companion object {

        val DEFAULT_FADING_SPEED_MILLIS = 300L;
        val DEFAULT_CACHE_SIZE = 32L * 1024L * 1024L // 32 MiB

        var fadingSpeedMillis = DEFAULT_FADING_SPEED_MILLIS
        private var cacheSizeMB = DEFAULT_CACHE_SIZE
        var debug = false
        var log = false

        private var okHttpClient: OkHttpClient? = null

        @Synchronized
        fun setCacheSize(cacheSizeMB: Long) {
            destroyOkHttpClient()
            this.cacheSizeMB = cacheSizeMB
        }

        @Synchronized
        internal fun getOkHttpClient(context: Context): OkHttpClient {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient()
                        .newBuilder()
                        .cache(getCache(context))
                        .build()
            }
            return okHttpClient!!
        }

        private inline fun getCache(context: Context): Cache? {
            if (cacheSizeMB > 0) {
                return Cache(getDefaultCacheDir(context), DEFAULT_CACHE_SIZE)
            } else {
                return null
            }
        }

        private inline fun getDefaultCacheDir(context: Context): File {
            val cacheDir = File(context.cacheDir, "humbleviewhttpcache")
            cacheDir.mkdirs()
            return cacheDir
        }

        private inline fun destroyOkHttpClient() {
            AsyncTask.execute {
                synchronized(this) {
                    okHttpClient?.dispatcher()?.executorService()?.shutdown()
                    okHttpClient?.connectionPool()?.evictAll()
                    okHttpClient?.cache()?.delete()
                    okHttpClient = null
                }
            }
        }
    }
}