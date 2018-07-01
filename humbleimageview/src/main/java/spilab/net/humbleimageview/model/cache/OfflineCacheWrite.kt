package spilab.net.humbleimageview.model.cache

import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File

internal class OfflineCacheWrite(private val cacheDirectory: File,
                                 private val fileName: String,
                                 private val data: ByteArray,
                                 private val uiThreadHandler: AndroidHandler,
                                 private val offlineCacheInterfaceWriteListener: OfflineCacheInterface.OfflineCacheWriteListener) : Runnable {

    override fun run() {
        val file = File(cacheDirectory, fileName)
        file.writeBytes(data)
        uiThreadHandler.post(Runnable { offlineCacheInterfaceWriteListener.onFileWriteComplete() })
    }
}