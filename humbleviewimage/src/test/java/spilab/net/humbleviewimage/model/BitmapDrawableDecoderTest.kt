package spilab.net.humbleviewimage.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert
import org.junit.Test
import spilab.net.humbleviewimage.android.AndroidBitmapFactory
import java.io.ByteArrayInputStream

class BitmapDrawableDecoderTest {

    private val mockResources = mockk<Resources>()

    @Test
    fun `Given a view and bitmap of the same size, When decode for view, Then the bitmap should not be scaled`() {
        val mockAndroidBitmapFactory = createAndroidBitmapFactoryMock(128, 64)
        val bitmapDrawableDecoder = BitmapDrawableDecoder(mockAndroidBitmapFactory)

        bitmapDrawableDecoder.decodeBitmapDrawableForViewSize(
                ByteArrayInputStream(ByteArray(8)),
                mockResources,
                HumbleBitmapId("url", ViewSize(128, 64)))

        verify { mockAndroidBitmapFactory.decodeBitmap(any(), 1) }
    }

    @Test
    fun `Given a view and a bigger bitmap, When decode for view, Then the bitmap should be scaled`() {
        val bitmapBiggerThanViewBy = 4
        val mockAndroidBitmapFactory = createAndroidBitmapFactoryMock(
                128 * bitmapBiggerThanViewBy,
                64 * bitmapBiggerThanViewBy)
        val bitmapDrawableDecoder = BitmapDrawableDecoder(mockAndroidBitmapFactory)

        bitmapDrawableDecoder.decodeBitmapDrawableForViewSize(
                ByteArrayInputStream(ByteArray(8)),
                mockResources,
                HumbleBitmapId("url", ViewSize(128, 64)))

        verify { mockAndroidBitmapFactory.decodeBitmap(any(), bitmapBiggerThanViewBy) }
    }

    @Test
    fun `Given a view and a smaller bitmap, When decode for view, Then the bitmap should not be scaled`() {
        val bitmapSmallerThanViewBy = 2
        val mockAndroidBitmapFactory = createAndroidBitmapFactoryMock(
                128 / bitmapSmallerThanViewBy,
                64 / bitmapSmallerThanViewBy)
        val bitmapDrawableDecoder = BitmapDrawableDecoder(mockAndroidBitmapFactory)

        bitmapDrawableDecoder.decodeBitmapDrawableForViewSize(
                ByteArrayInputStream(ByteArray(8)),
                mockResources,
                HumbleBitmapId("url", ViewSize(128, 64)))

        verify { mockAndroidBitmapFactory.decodeBitmap(any(), 1) }
    }

    @Test
    fun `Given a view and bitmap of the same size, When decode for view, Then fill the drawable with the right bitmap id`() {
        val mockAndroidBitmapFactory = createAndroidBitmapFactoryMock(128, 64)
        val bitmapDrawableDecoder = BitmapDrawableDecoder(mockAndroidBitmapFactory)

        val expectedId = HumbleBitmapId("url test", ViewSize(128, 64))
        val drawable = bitmapDrawableDecoder.decodeBitmapDrawableForViewSize(
                ByteArrayInputStream(ByteArray(8)),
                mockResources,
                expectedId)

        Assert.assertEquals(expectedId, drawable?.humbleBitmapId)
    }

    private fun createAndroidBitmapFactoryMock(width: Int, height: Int): AndroidBitmapFactory {
        val mockBitmap = mockk<Bitmap>()
        val options = BitmapFactory.Options().apply {
            outWidth = width
            outHeight = height
        }
        val mockAndroidBitmapFactory = mockk<AndroidBitmapFactory>()
        every { mockAndroidBitmapFactory.decodeBitmapSize(any()) } returns options
        every { mockAndroidBitmapFactory.decodeBitmap(any(), any()) } returns mockBitmap
        return mockAndroidBitmapFactory
    }
}
