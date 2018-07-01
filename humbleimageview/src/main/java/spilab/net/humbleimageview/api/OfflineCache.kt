package spilab.net.humbleimageview.api

import android.content.Context
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.model.cache.DefaultOfflineCache
import spilab.net.humbleimageview.model.cache.OfflineCacheInterface
import java.io.File

class OfflineCache internal constructor(private val executorProvider: ExecutorProvider,
                                        private val androidHandler: AndroidHandler) {

    private var cacheDirectory: File? = null
    private var offlineCache: OfflineCacheInterface? = null

    fun setCacheDirectory(directory: File) {
        cacheDirectory = directory
    }

    fun setOfflineCache(offlineCacheInterface: OfflineCacheInterface) {
        offlineCache = offlineCacheInterface
    }

    fun reset() {
        offlineCache = null
    }

    internal fun getOfflineCache(appContext: Context): OfflineCacheInterface {
        if (offlineCache == null) {
            val cache = DefaultOfflineCache(getCacheDirectory(appContext),
                    executorProvider.getExecutorService(), androidHandler)
            offlineCache = cache
        }
        return offlineCache!!
    }

    private fun getCacheDirectory(appContext: Context): File {
        if (cacheDirectory == null) {
            cacheDirectory = getDefaultCacheDirectory(appContext)
        }
        return cacheDirectory!!
    }

    private inline fun getDefaultCacheDirectory(appContext: Context): File {
        val cacheDir = File(appContext.filesDir, HumbleViewAPI.CACHE_DIRECTORY)
        cacheDir.mkdirs()
        return cacheDir
    }
}