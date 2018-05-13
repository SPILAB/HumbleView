package spilab.net.humbleviewimage.model

import android.content.Context
import android.net.http.HttpResponseCache
import android.util.Log
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newFixedThreadPool
import kotlin.math.max

internal object HumbleViewExecutor {

    private var httpCacheInstaller: Boolean = false

    val executorService: ExecutorService by lazy {
        val threadCount = max(2, Runtime.getRuntime().availableProcessors() - 1)
        return@lazy newFixedThreadPool(threadCount)
    }

    // TODO: REMOVE THIS CODE LATER IT IS JUST A POC
    fun installHTTPCache(context: Context) {
        if (!httpCacheInstaller) {
            try {
                val httpCacheDir = File(context.cacheDir, "http")
                val httpCacheSize = 64L * 1024L * 1024L
                HttpResponseCache.install(httpCacheDir, httpCacheSize)
            } catch (exception: IOException) {
                Log.i("TAG", "HTTP response cache installation failed: $exception")
            }
            httpCacheInstaller = true
        }
    }
}