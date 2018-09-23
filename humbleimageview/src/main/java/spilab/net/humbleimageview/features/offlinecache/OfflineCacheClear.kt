package spilab.net.humbleimageview.features.offlinecache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheClear(private val cacheDirectory: File,
                                 private val uiThreadHandler: AndroidHandler,
                                 private val offlineCacheInterfaceClearListener: OfflineCacheInterface.OfflineCacheClearListener) : Runnable {

    override fun run() {
        cacheDirectory.deleteRecursively()
        uiThreadHandler.post(Runnable { offlineCacheInterfaceClearListener.onCacheCleared() })
    }
}