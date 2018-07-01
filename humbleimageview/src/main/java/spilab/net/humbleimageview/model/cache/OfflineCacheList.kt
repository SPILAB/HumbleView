package spilab.net.humbleimageview.model.cache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheList(private val cacheDirectory: File,
                                private val uiThreadHandler: AndroidHandler,
                                private val offlineCacheInterfaceListListener: OfflineCacheInterface.OfflineCacheListListener) : Runnable {

    override fun run() {
        val filesList = mutableListOf<File>()
        cacheDirectory.walk().filter { it.isFile }.forEach {
            filesList.add(it)
        }
        uiThreadHandler.post(Runnable { offlineCacheInterfaceListListener.onFileList(filesList.toList()) })
    }
}