package spilab.net.humbleimageview.model.bitmap

import android.graphics.Bitmap
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class BitmapPoolTest {

    @Test
    fun `Given a empty bitmap pool, When find a bitmap, Then should return null`() {
        Assert.assertNull(BitmapPool.find(100, 100))
    }

    @Test
    fun `Given bitmap pool filled with one bitmap, When find a bitmap, Then should return the bitmap`() {
        val mockBitmap100x100 = createMockBitmap(100, 100, 100 * 100 * 4)
        BitmapPool.put(mockBitmap100x100)
        Assert.assertEquals(mockBitmap100x100, BitmapPool.find(100, 100))
        Assert.assertNull(BitmapPool.find(100, 100))
    }

    @Test
    fun `Given bitmap pool filled with two bitmaps, When find a bitmap, Then should return the smallest bitmap possible`() {
        val mockBitmap200x200 = createMockBitmap(200, 200, 200 * 200 * 4)
        val mockBitmap150x200 = createMockBitmap(150, 200, 150 * 200 * 4)
        val mockBitmap200x100 = createMockBitmap(200, 100, 200 * 100 * 4)
        val mockBitmap100x100 = createMockBitmap(100, 100, 100 * 100 * 4)
        BitmapPool.put(mockBitmap150x200)
        BitmapPool.put(mockBitmap200x200)
        BitmapPool.put(mockBitmap100x100)
        BitmapPool.put(mockBitmap200x100)
        Assert.assertEquals(mockBitmap100x100, BitmapPool.find(100, 100))
        Assert.assertEquals(mockBitmap200x100, BitmapPool.find(100, 100))
        Assert.assertEquals(mockBitmap150x200, BitmapPool.find(100, 100))
        Assert.assertEquals(mockBitmap200x200, BitmapPool.find(100, 100))
        Assert.assertNull(BitmapPool.find(100, 100))
    }

    private fun createMockBitmap(width: Int, height: Int, allocated: Int): Bitmap {
        val mockBitmap: Bitmap = mockk()
        every { mockBitmap.width } returns width
        every { mockBitmap.height } returns height
        every { mockBitmap.allocationByteCount } returns allocated
        return mockBitmap
    }
}