package spilab.net.humbleimageview.model.cache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheList(private val cacheDirectory: File,
                                private val uiThreadHandler: AndroidHandler,
                                private val offlineCacheListListener: OfflineCacheListListener) : Runnable {

    interface OfflineCacheListListener {
        fun onFileList(filesList: List<String>)

    }

    override fun run() {
        val filesList = mutableListOf<String>()
        cacheDirectory.walk().filter { it.isFile }.forEach {
            filesList.add(it.name)
        }
        uiThreadHandler.post(Runnable { offlineCacheListListener.onFileList(filesList.toList()) })
    }
}