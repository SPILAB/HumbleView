package spilab.net.humbleimageview.api

import android.os.Handler
import android.os.Looper
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import spilab.net.humbleimageview.common.AndroidHandlerMock
import spilab.net.humbleimageview.common.SynchronousOfflineCache
import spilab.net.humbleimageview.features.offlinecache.OfflineCacheInterface
import java.io.File
import java.util.concurrent.Future


@RunWith(AndroidJUnit4::class)
class OfflineCacheTest {

    private lateinit var synchronousOfflineCache: SynchronousOfflineCache
    private lateinit var offlineCache: OfflineCache

    @Before
    fun beforeTests() {
        synchronousOfflineCache = SynchronousOfflineCache()
        val mockExecutorProvider = MockExecutorProvider(synchronousOfflineCache.mockExecutorService)
        offlineCache = OfflineCache(mockExecutorProvider, AndroidHandlerMock(Handler(Looper.getMainLooper())))
    }

    @After
    fun afterTests() {
        val appContext = InstrumentationRegistry.getTargetContext()
        synchronousOfflineCache.clear(offlineCache.getOfflineCache(appContext))
    }

    @Test
    fun GIVEN_default_parameters_WHEN_put_in_cache_THEN_put_in_default_cache_directory() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val cache = offlineCache.getOfflineCache(appContext)
        synchronousOfflineCache.put(cache, "testkey")
        val files = synchronousOfflineCache.list(cache)
        Assert.assertEquals(1, files.size)
        Assert.assertTrue(files[0].parent.contains("files/humbleviewcache"))
    }

    @Test
    fun GIVEN_a_cache_directory_WHEN_put_in_cache_THEN_put_in_the_specified_directory() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val cacheDir = File(appContext.filesDir, "testhumbleviewcache")
        cacheDir.mkdirs()
        offlineCache.setCacheDirectory(cacheDir)
        val cache = offlineCache.getOfflineCache(appContext)
        synchronousOfflineCache.put(cache, "testkey")
        val files = synchronousOfflineCache.list(cache)
        Assert.assertEquals(1, files.size)
        Assert.assertTrue(files[0].parent.contains("files/testhumbleviewcache"))
    }

    @Test
    fun GIVEN_a_custom_cache_WHEN_put_in_cache_THEN_use_the_custom_cache() {
        val mockFuture: Future<String> = mockk()
        offlineCache.setOfflineCache(createCustomCache(mockFuture))
        val appContext = InstrumentationRegistry.getTargetContext()
        val cache = offlineCache.getOfflineCache(appContext)
        Assert.assertTrue(synchronousOfflineCache.put(cache, "testkey"))
        offlineCache.reset()
    }

    private fun createCustomCache(mockFuture: Future<String>): OfflineCacheInterface {
        return object : OfflineCacheInterface {
            override fun put(key: String, data: ByteArray, offlineCacheWriteListener: OfflineCacheInterface.OfflineCacheWriteListener): Future<*> {
                offlineCacheWriteListener.onFileWriteComplete()
                return mockFuture
            }

            override fun get(key: String, offlineCacheWriteListener: OfflineCacheInterface.OfflineCacheReadListener): Future<*> {
                offlineCacheWriteListener.onFileNotFound()
                return mockFuture
            }

            override fun list(offlineCacheListListener: OfflineCacheInterface.OfflineCacheListListener): Future<*> {
                offlineCacheListListener.onFileList(emptyList())
                return mockFuture
            }

            override fun clear(offlineCacheClearListener: OfflineCacheInterface.OfflineCacheClearListener): Future<*> {
                offlineCacheClearListener.onCacheCleared()
                return mockFuture
            }
        }
    }
}