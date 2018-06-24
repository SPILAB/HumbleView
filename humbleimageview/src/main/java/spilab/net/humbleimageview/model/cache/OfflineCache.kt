package spilab.net.humbleimageview.model.cache

import okio.ByteString
import spilab.net.humbleimageview.android.AndroidHandler
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

internal class OfflineCache(private val cacheDirectory: File,
                            private var executorService: ExecutorService,
                            private val uiThreadHandler: AndroidHandler) {


    internal fun put(key: String, data: ByteArray,
                     offlineCacheWriteListener: OfflineCacheWrite.OfflineCacheWriteListener): Future<*> {
        return executorService.submit(OfflineCacheWrite(cacheDirectory, getFileName(key), data,
                uiThreadHandler, offlineCacheWriteListener))
    }

    internal fun get(key: String,
                     offlineCacheWriteListener: OfflineCacheRead.OfflineCacheReadListener): Future<*> {
        return executorService.submit(OfflineCacheRead(cacheDirectory, getFileName(key),
                uiThreadHandler, offlineCacheWriteListener))
    }

    internal fun list(offlineCacheListListener: OfflineCacheList.OfflineCacheListListener): Future<*> {
        return executorService.submit(OfflineCacheList(cacheDirectory, uiThreadHandler,
                offlineCacheListListener))
    }

    internal fun clear(offlineCacheClearListener: OfflineCacheClear.OfflineCacheClearListener): Future<*> {
        return executorService.submit(OfflineCacheClear(cacheDirectory, uiThreadHandler,
                offlineCacheClearListener))
    }

    private inline fun getFileName(key: String): String {
        return ByteString.encodeUtf8(key).md5().hex()
    }
}