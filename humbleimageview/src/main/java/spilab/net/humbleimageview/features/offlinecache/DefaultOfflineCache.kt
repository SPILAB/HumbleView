package spilab.net.humbleimageview.features.offlinecache

import okio.ByteString
import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

internal class DefaultOfflineCache(private val cacheDirectory: File,
                                   private var executorService: ExecutorService,
                                   private val uiThreadHandler: AndroidHandler) : OfflineCacheInterface {


    override fun put(key: String, data: ByteArray,
                     offlineCacheInterfaceWriteListener: OfflineCacheInterface.OfflineCacheWriteListener): Future<*> {
        return executorService.submit(OfflineCacheWrite(cacheDirectory, getFileName(key), data,
                uiThreadHandler, offlineCacheInterfaceWriteListener))
    }

    override fun get(key: String,
                     offlineCacheInterfaceWriteListener: OfflineCacheInterface.OfflineCacheReadListener): Future<*> {
        return executorService.submit(OfflineCacheRead(cacheDirectory, getFileName(key),
                uiThreadHandler, offlineCacheInterfaceWriteListener))
    }

    override fun list(offlineCacheInterfaceListListener: OfflineCacheInterface.OfflineCacheListListener): Future<*> {
        return executorService.submit(OfflineCacheList(cacheDirectory, uiThreadHandler,
                offlineCacheInterfaceListListener))
    }

    override fun clear(offlineCacheInterfaceClearListener: OfflineCacheInterface.OfflineCacheClearListener): Future<*> {
        return executorService.submit(OfflineCacheClear(cacheDirectory, uiThreadHandler,
                offlineCacheInterfaceClearListener))
    }

    private fun getFileName(key: String): String {
        return ByteString.encodeUtf8(key).md5().hex()
    }
}