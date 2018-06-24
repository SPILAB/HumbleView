package spilab.net.humbleimageview.model.cache

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import spilab.net.humbleimageview.model.cache.OfflineCacheWrite.OfflineCacheWriteListener
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future


@RunWith(AndroidJUnit4::class)
class OfflineCacheTest {

    private val mockFutureTask = mockk<Future<*>>()
    private val mockExecutorService = mockk<ExecutorService>()
    private val captureExecutorRunnable: CapturingSlot<Runnable> = slot()

    private lateinit var offlineCache: OfflineCache

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getTargetContext()
        every {
            mockExecutorService.submit(
                    capture(captureExecutorRunnable)
            )
        } returns mockFutureTask
        offlineCache = OfflineCache(getDefaultCacheDirectory(appContext),
                mockExecutorService, AndroidHandlerMock(Handler(Looper.getMainLooper())))
    }

    @After
    fun teardown() {
        Assert.assertTrue(clear())
    }

    @Test
    fun GIVEN_some_data_WHEN_put_in_cache_THEN_should_write_on_disk() {
        Assert.assertTrue(put("https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/"))
        var files = list()
        Assert.assertEquals(1, files.size)
    }

    @Test
    fun GIVEN_an_not_existing_file_WHEN_get_THEN_should_not_return_data() {
        var data = get("https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/")
        Assert.assertNull(data)
    }

    @Test
    fun GIVEN_an_existing_file_WHEN_get_THEN_should_return_data() {
        val key = "https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/"
        put(key)
        var data = get(key)
        Assert.assertEquals("[0, 1, 2, 3, 4]", data?.asList().toString())
    }

    @Test
    fun GIVEN_two_files_WHEN_list_THEN_should_return_two_file_name() {
        Assert.assertTrue(put("https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/"))
        Assert.assertTrue(put("https://www.flickr.com/photos/110273693@N04/26269766220/sizes/k/"))
        var files = list()
        Assert.assertEquals(2, files.size)
    }

    private fun put(key: String): Boolean {
        var completed = false
        offlineCache.put(key, byteArrayOf(0, 1, 2, 3, 4), object : OfflineCacheWriteListener {
            override fun onFileWriteComplete() {
                completed = true
            }
        })
        captureExecutorRunnable.captured.run()
        return completed
    }

    private fun list(): List<String> {
        var files = emptyList<String>()
        offlineCache.list(object : OfflineCacheList.OfflineCacheListListener {
            override fun onFileList(filesList: List<String>) {
                files = filesList
            }
        })
        captureExecutorRunnable.captured.run()
        return files
    }

    private fun get(key: String): ByteArray? {
        var dataRead: ByteArray? = null
        offlineCache.get(key, object : OfflineCacheRead.OfflineCacheReadListener {
            override fun onFileRead(data: ByteArray) {
                dataRead = data
            }

            override fun onFileNotFound() {}
        })
        captureExecutorRunnable.captured.run()
        return dataRead
    }

    private fun clear(): Boolean {
        var completed = false
        offlineCache.clear(object : OfflineCacheClear.OfflineCacheClearListener {
            override fun onCacheCleared() {
                completed = true
            }
        })
        captureExecutorRunnable.captured.run()
        return completed
    }

    private inline fun getDefaultCacheDirectory(appContext: Context): File {
        val cacheDir = File(appContext.filesDir, "humbleviewcachetest")
        cacheDir.mkdirs()
        return cacheDir
    }
}