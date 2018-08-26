package spilab.net.humbleimageview.features.decode

import android.graphics.BitmapFactory.Options
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidBitmapFactory

class BitmapDecodeWithScaleTest {

    private lateinit var bitmapDecode: BitmapDecodeWithScale
    private lateinit var captureOptions: CapturingSlot<Options>
    private var mockAndroidBitmapFactory = mockk<AndroidBitmapFactory>()

    private var bitmapWidth = 0
    private var bitmapHeight = 0
    private var inSampleSize = 0

    @Before
    fun beforeTests() {
        inSampleSize = 0
        captureOptions = slot()
        every { mockAndroidBitmapFactory.decodeByteArray(any(), any(), any(), capture(captureOptions)) } answers {
            if (captureOptions.captured.inJustDecodeBounds) {
                captureOptions.captured.outWidth = bitmapWidth
                captureOptions.captured.outHeight = bitmapHeight
            } else {
                inSampleSize = captureOptions.captured.inSampleSize
            }
            mockk()
        }
        bitmapDecode = BitmapDecodeWithScale(mockAndroidBitmapFactory)
    }

    @Test
    fun `Given a view and bitmap of the same size, When decode for view, Then the bitmap should not be scaled`() {
        bitmapWidth = 128
        bitmapHeight = 64
        bitmapDecode.decodeBitmapForSize(ByteArray(8), 128, 64)
        Assert.assertEquals(1, inSampleSize)
    }

    @Test
    fun `Given a view and a bigger bitmap, When decode for view, Then the bitmap should be scaled`() {
        val bitmapBiggerThanViewBy = 4
        bitmapWidth = 128 * bitmapBiggerThanViewBy
        bitmapHeight = 64 * bitmapBiggerThanViewBy
        bitmapDecode.decodeBitmapForSize(ByteArray(8), 128, 64)
        Assert.assertEquals(bitmapBiggerThanViewBy, inSampleSize)
    }

    @Test
    fun `Given a view and a smaller bitmap, When decode for view, Then the bitmap should not be scaled`() {
        val bitmapSmallerThanViewBy = 2
        bitmapWidth = 128 / bitmapSmallerThanViewBy
        bitmapHeight = 64 / bitmapSmallerThanViewBy
        bitmapDecode.decodeBitmapForSize(ByteArray(8), 128, 64)
        Assert.assertEquals(1, inSampleSize)
    }
}