package spilab.net.humbleimageview.api

import android.content.Context
import spilab.net.humbleimageview.model.cache.OfflineCache
import java.io.File

class Cache {

    private var cacheDirectory: File? = null

    fun setCacheDirectory(directory: File) {
        cacheDirectory = directory
    }

    private var offlineCache: OfflineCache? = null

    internal fun getOfflineCache(appContext: Context): OfflineCache {
        if (offlineCache == null) {
            offlineCache = OfflineCache(getCacheDirectory(appContext))
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