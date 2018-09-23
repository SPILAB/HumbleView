package spilab.net.humbleimageview.common

import android.support.test.InstrumentationRegistry
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk

import io.mockk.slot
import spilab.net.humbleimageview.features.offlinecache.OfflineCacheInterface
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class SynchronousOfflineCache {

    val mockExecutorService = mockk<ExecutorService>()

    private val mockFutureTask = mockk<Future<*>>()
    private val captureExecutorRunnable: CapturingSlot<Runnable> = slot()

    init {
        val appContext = InstrumentationRegistry.getTargetContext()
        every {
            mockExecutorService.submit(
                    capture(captureExecutorRunnable)
            )
        } returns mockFutureTask
    }

    fun put(offlineCacheInterface: OfflineCacheInterface, key: String): Boolean {
        var completed = false
        offlineCacheInterface.put(key, byteArrayOf(0, 1, 2, 3, 4), object : OfflineCacheInterface.OfflineCacheWriteListener {
            override fun onFileWriteComplete() {
                completed = true
            }
        })
        if (captureExecutorRunnable.isCaptured) {
            captureExecutorRunnable.captured.run()
        }
        return completed
    }

    fun list(offlineCacheInterface: OfflineCacheInterface): List<File> {
        var files = emptyList<File>()
        offlineCacheInterface.list(object : OfflineCacheInterface.OfflineCacheListListener {
            override fun onFileList(filesList: List<File>) {
                files = filesList
            }
        })
        captureExecutorRunnable.captured.run()
        return files
    }

    fun get(offlineCacheInterface: OfflineCacheInterface, key: String): ByteArray? {
        var dataRead: ByteArray? = null
        offlineCacheInterface.get(key, object : OfflineCacheInterface.OfflineCacheReadListener {
            override fun onFileRead(data: ByteArray) {
                dataRead = data
            }

            override fun onFileNotFound() {}
        })
        captureExecutorRunnable.captured.run()
        return dataRead
    }

    fun clear(offlineCacheInterface: OfflineCacheInterface): Boolean {
        var completed = false
        offlineCacheInterface.clear(object : OfflineCacheInterface.OfflineCacheClearListener {
            override fun onCacheCleared() {
                completed = true
            }
        })
        if (captureExecutorRunnable.isCaptured) {
            captureExecutorRunnable.captured.run()
        }
        return completed
    }
}