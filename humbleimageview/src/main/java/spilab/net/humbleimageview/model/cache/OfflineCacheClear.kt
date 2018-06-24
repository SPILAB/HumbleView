package spilab.net.humbleimageview.model.cache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheClear(private val cacheDirectory: File,
                                 private val uiThreadHandler: AndroidHandler,
                                 private val offlineCacheClearListener: OfflineCacheClearListener) : Runnable {

    interface OfflineCacheClearListener {
        fun onCacheCleared()
    }

    override fun run() {
        cacheDirectory.deleteRecursively()
        uiThreadHandler.post(Runnable { offlineCacheClearListener.onCacheCleared() })
    }
}