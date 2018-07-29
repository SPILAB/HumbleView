package spilab.net.humbleimageview.features.memory

import android.graphics.Bitmap
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Test

class BitmapPoolTest {

    @After
    fun tearDown() {
        BitmapPool.clear()
    }

    @Test
    fun `Given a empty bitmap pool, When find a bitmap, Then should return null`() {
        BitmapPool.bitmapRecycle = BitmapRecycle(19)
        Assert.assertNull(BitmapPool.find(100, 100, 1))
    }

    @Test
    fun `Given bitmap pool filled with one bitmap, When find a bitmap, Then should return the bitmap`() {
        BitmapPool.bitmapRecycle = BitmapRecycle(19)
        val mockBitmap100x100 = createMockBitmap(100, 100)
        BitmapPool.put(mockBitmap100x100)
        Assert.assertEquals(mockBitmap100x100, BitmapPool.find(100, 100, 1))
        Assert.assertNull(BitmapPool.find(100, 100, 1))
    }

    @Test
    fun `Given bitmap pool filled with fours bitmaps, When find a bitmap, Then should return the smallest bitmap possible`() {
        BitmapPool.bitmapRecycle = BitmapRecycle(19)
        val mockBitmap200x200 = createMockBitmap(200, 200)
        val mockBitmap150x200 = createMockBitmap(150, 200)
        val mockBitmap200x100 = createMockBitmap(200, 100)
        val mockBitmap100x100 = createMockBitmap(100, 100)
        BitmapPool.put(mockBitmap150x200)
        BitmapPool.put(mockBitmap200x200)
        BitmapPool.put(mockBitmap100x100)
        BitmapPool.put(mockBitmap200x100)
        Assert.assertEquals(mockBitmap100x100, BitmapPool.find(100, 100, 1))
        Assert.assertEquals(mockBitmap200x100, BitmapPool.find(100, 100, 1))
        Assert.assertEquals(mockBitmap150x200, BitmapPool.find(100, 100, 1))
        Assert.assertEquals(mockBitmap200x200, BitmapPool.find(100, 100, 1))
        Assert.assertNull(BitmapPool.find(100, 100, 1))
    }

    @Test
    fun `Given an API level 16 and a bitmap pool filled with two bitmaps, When find a bitmap, Then should return only exact matching bitmap`() {
        BitmapPool.bitmapRecycle = BitmapRecycle(16)
        val mockBitmap200x200 = createMockBitmap(200, 200)
        val mockBitmap150x200 = createMockBitmap(150, 200)
        val mockBitmap200x100 = createMockBitmap(200, 100)
        val mockBitmap100x100 = createMockBitmap(100, 100)
        BitmapPool.put(mockBitmap150x200)
        BitmapPool.put(mockBitmap200x200)
        BitmapPool.put(mockBitmap100x100)
        BitmapPool.put(mockBitmap200x100)
        Assert.assertEquals(mockBitmap100x100, BitmapPool.find(100, 100, 1))
        Assert.assertEquals(mockBitmap200x200, BitmapPool.find(200, 200, 1))
        Assert.assertNull(BitmapPool.find(150, 100, 1))
        Assert.assertNull(BitmapPool.find(150, 200, 2))
    }

    private fun createMockBitmap(width: Int, height: Int): Bitmap {
        val mockBitmap: Bitmap = mockk()
        every { mockBitmap.width } returns width
        every { mockBitmap.height } returns height
        every { mockBitmap.allocationByteCount } returns width * height * 4
        every { mockBitmap.config } returns Bitmap.Config.ARGB_8888
        return mockBitmap
    }
}