package spilab.net.humbleimageview.features.offlinecache

import java.io.File
import java.util.concurrent.Future

interface OfflineCacheInterface {

    interface OfflineCacheClearListener {
        fun onCacheCleared()
    }

    interface OfflineCacheWriteListener {
        fun onFileWriteComplete()
    }

    interface OfflineCacheReadListener {
        fun onFileRead(data: ByteArray)
        fun onFileNotFound()
    }

    interface OfflineCacheListListener {
        fun onFileList(filesList: List<File>)
    }

    fun put(key: String, data: ByteArray,
            offlineCacheWriteListener: OfflineCacheWriteListener): Future<*>

    fun get(key: String,
            offlineCacheWriteListener: OfflineCacheReadListener): Future<*>

    fun list(offlineCacheListListener: OfflineCacheListListener): Future<*>
    fun clear(offlineCacheClearListener: OfflineCacheClearListener): Future<*>
}