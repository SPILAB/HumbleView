package spilab.net.humbleimageview.api

import android.content.Context
import android.os.AsyncTask
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File

class Http(private val executorProvider: ExecutorProvider) {

    companion object {
        private const val DEFAULT_CACHE_SIZE = 32L * 1024L * 1024L // 32 MiB
    }

    private var cacheSizeMB = DEFAULT_CACHE_SIZE
    private var okHttpClient: OkHttpClient? = null

    @Synchronized
    fun setHttpCacheSize(cacheSizeMB: Long) {
        destroyOkHttpClient()
        this.cacheSizeMB = cacheSizeMB
    }

    @Synchronized
    internal fun getOkHttpClient(context: Context): OkHttpClient {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient()
                    .newBuilder()
                    .dispatcher(Dispatcher(executorProvider.getExecutorService()))
                    .cache(getHttpCache(context))
                    .build()
        }
        return okHttpClient!!
    }

    private inline fun getHttpCache(context: Context): Cache? {
        return if (cacheSizeMB > 0) {
            Cache(getDefaultHttpCacheDir(context), DEFAULT_CACHE_SIZE)
        } else {
            null
        }
    }

    private inline fun getDefaultHttpCacheDir(context: Context): File {
        val cacheDir = File(context.cacheDir, HumbleViewAPI.HTTP_CACHE_DIRECTORY)
        cacheDir.mkdirs()
        return cacheDir
    }

    private inline fun destroyOkHttpClient() {
        AsyncTask.execute {
            synchronized(this) {
                executorProvider.reset()
                okHttpClient?.connectionPool()?.evictAll()
                okHttpClient?.cache()?.delete()
                okHttpClient = null
            }
        }
    }
}