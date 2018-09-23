package spilab.net.humbleimageview.model.cache

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import spilab.net.humbleimageview.common.AndroidHandlerMock
import spilab.net.humbleimageview.common.SynchronousOfflineCache
import spilab.net.humbleimageview.features.offlinecache.DefaultOfflineCache
import java.io.File


@RunWith(AndroidJUnit4::class)
class DefaultOfflineCacheTest {

    private lateinit var synchronousOfflineCache: SynchronousOfflineCache
    private lateinit var offlineCache: DefaultOfflineCache

    @Before
    fun beforeTests() {
        val appContext = InstrumentationRegistry.getTargetContext()
        synchronousOfflineCache = SynchronousOfflineCache()
        offlineCache = DefaultOfflineCache(getDefaultCacheDirectory(appContext),
                synchronousOfflineCache.mockExecutorService,
                AndroidHandlerMock(Handler(Looper.getMainLooper())))
    }

    @After
    fun afterTest() {
        Assert.assertTrue(synchronousOfflineCache.clear(offlineCache))
    }

    @Test
    fun GIVEN_some_data_WHEN_put_in_cache_THEN_should_write_on_disk() {
        Assert.assertTrue(synchronousOfflineCache.put(offlineCache,
                "https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/"))
        val files = synchronousOfflineCache.list(offlineCache)
        Assert.assertEquals(1, files.size)
    }

    @Test
    fun GIVEN_an_not_existing_file_WHEN_get_THEN_should_not_return_data() {
        val data = synchronousOfflineCache.get(offlineCache,
                "https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/")
        Assert.assertNull(data)
    }

    @Test
    fun GIVEN_an_existing_file_WHEN_get_THEN_should_return_data() {
        val key = "https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/"
        synchronousOfflineCache.put(offlineCache, key)
        val data = synchronousOfflineCache.get(offlineCache, key)
        Assert.assertEquals("[0, 1, 2, 3, 4]", data?.asList().toString())
    }

    @Test
    fun GIVEN_two_files_WHEN_list_THEN_should_return_two_file_name() {
        Assert.assertTrue(synchronousOfflineCache.put(offlineCache,
                "https://www.flickr.com/photos/110273693@N04/26269766220/sizes/m/"))
        Assert.assertTrue(synchronousOfflineCache.put(offlineCache,
                "https://www.flickr.com/photos/110273693@N04/26269766220/sizes/k/"))
        val files = synchronousOfflineCache.list(offlineCache)
        Assert.assertEquals(2, files.size)
    }

    private inline fun getDefaultCacheDirectory(appContext: Context): File {
        val cacheDir = File(appContext.filesDir, "humbleviewcachetest")
        cacheDir.mkdirs()
        return cacheDir
    }
}