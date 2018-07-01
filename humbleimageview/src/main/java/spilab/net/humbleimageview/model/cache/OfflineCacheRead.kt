package spilab.net.humbleimageview.model.cache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheRead(private val cacheDirectory: File,
                                private val fileName: String,
                                private val uiThreadHandler: AndroidHandler,
                                private val offlineCacheInterfaceReadListener: OfflineCacheInterface.OfflineCacheReadListener) : Runnable {

    override fun run() {
        val file = File(cacheDirectory, fileName)
        if (file.exists()) {
            val data = file.readBytes()
            uiThreadHandler.post(Runnable { offlineCacheInterfaceReadListener.onFileRead(data) })
        } else {
            uiThreadHandler.post(Runnable { offlineCacheInterfaceReadListener.onFileNotFound() })
        }
    }
}