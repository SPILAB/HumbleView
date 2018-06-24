package spilab.net.humbleimageview.model.cache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheWrite(private val cacheDirectory: File,
                                 private val fileName: String,
                                 private val data: ByteArray,
                                 private val uiThreadHandler: AndroidHandler,
                                 private val offlineCacheWriteListener: OfflineCacheWriteListener) : Runnable {

    interface OfflineCacheWriteListener {
        fun onFileWriteComplete()
    }

    override fun run() {
        val file = File(cacheDirectory, fileName)
        file.writeBytes(data)
        uiThreadHandler.post(Runnable { offlineCacheWriteListener.onFileWriteComplete() })
    }
}