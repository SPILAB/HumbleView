package spilab.net.humbleviewimage.view


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import spilab.net.humbleviewimage.android.AndroidBitmapFactory
import java.io.ByteArrayInputStream


class HumbleViewSizeTest {

    @Test
    fun `Given a view and bitmap of the same size, When decode for view, Then the bitmap should not be scaled`() {
        val mockAndroidBitmapFactory = createAndroidBitmapFactoryMock(128, 64)
        val humbleViewSize = HumbleViewSize(128, 64, mockAndroidBitmapFactory)

        humbleViewSize.decodeBitmapForViewSize(ByteArrayInputStream(ByteArray(8)))
        verify { mockAndroidBitmapFactory.decodeBitmap(any(), 1) }
    }

    @Test
    fun `Given a view and a bigger bitmap, When decode for view, Then the bitmap should be scaled`() {
        val bitmapBiggerThanViewBy = 4
        val mockAndroidBitmapFactory = createAndroidBitmapFactoryMock(
                128 * bitmapBiggerThanViewBy,
                64 * bitmapBiggerThanViewBy)

        val humbleViewSize = HumbleViewSize(128, 64, mockAndroidBitmapFactory)

        humbleViewSize.decodeBitmapForViewSize(ByteArrayInputStream(ByteArray(8)))
        verify { mockAndroidBitmapFactory.decodeBitmap(any(), bitmapBiggerThanViewBy) }
    }

    @Test
    fun `Given a view and a smaller bitmap, When decode for view, Then the bitmap should not be scaled`() {
        val bitmapSmallerThanViewBy = 2
        val mockAndroidBitmapFactory = createAndroidBitmapFactoryMock(
                128 / bitmapSmallerThanViewBy,
                64 / bitmapSmallerThanViewBy)

        val humbleViewSize = HumbleViewSize(128, 64, mockAndroidBitmapFactory)

        humbleViewSize.decodeBitmapForViewSize(ByteArrayInputStream(ByteArray(8)))
        verify { mockAndroidBitmapFactory.decodeBitmap(any(), 1) }
    }

    private fun createAndroidBitmapFactoryMock(width: Int, height: Int): AndroidBitmapFactory {
        val mockBitamp = mockk<Bitmap>()
        val options = BitmapFactory.Options().apply {
            outWidth = width
            outHeight = height
        }
        val mockAndroidBitmapFactory = mockk<AndroidBitmapFactory>()
        every { mockAndroidBitmapFactory.decodeBitmapSize(any()) } returns options
        every { mockAndroidBitmapFactory.decodeBitmap(any(), any()) } returns mockBitamp
        return mockAndroidBitmapFactory
    }
}