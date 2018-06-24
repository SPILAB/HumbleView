package spilab.net.humbleimageview.model.cache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheRead(private val cacheDirectory: File,
                                private val fileName: String,
                                private val uiThreadHandler: AndroidHandler,
                                private val offlineCacheReadListener: OfflineCacheReadListener) : Runnable {

    interface OfflineCacheReadListener {
        fun onFileRead(data: ByteArray)
        fun onFileNotFound()
    }

    override fun run() {
        val file = File(cacheDirectory, fileName)
        if (file.exists()) {
            val data = file.readBytes()
            uiThreadHandler.post(Runnable { offlineCacheReadListener.onFileRead(data) })
        } else {
            uiThreadHandler.post(Runnable { offlineCacheReadListener.onFileNotFound() })
        }
    }
}