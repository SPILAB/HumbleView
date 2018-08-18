package spilab.net.humbleimageview.features.memory

import android.graphics.Bitmap
import android.widget.ImageView
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

class DrawableRecyclerTest {

    @Before
    fun beforeTests() {
        mockkObject(BitmapPool)
        mockkObject(VectorDrawablePool)
    }

    @After
    fun afterTests() {
        unmockkAll()
        BitmapPool.clear()
        VectorDrawablePool.clear()
    }

    @Test
    fun `Given an ImageViewDrawable with a HumbleBitmapDrawable, When recycle, Then should recycle the bitmap`() {
        val mockBitmap = mockk<Bitmap>()
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.bitmap } returns mockBitmap
        val mockImageViewDrawable = mockk<ImageViewDrawable>(relaxed = true)
        every { mockImageViewDrawable.getDrawable() } returns mockHumbleBitmapDrawable

        DrawableRecycler().recycleImageViewDrawable(mockImageViewDrawable)
        verify { mockImageViewDrawable.setDrawable(null) }
        verify { BitmapPool.put(mockBitmap) }
    }

    @Test
    fun `Given an ImageView with a DrawableFromResId, When recycle, Then should recycle the bitmap`() {
        val mockImageViewDrawable = mockk<VectorDrawableFromResId>(relaxed = true)
        val mockImageView = mockk<ImageView>()
        every { mockImageView.drawable } returns mockImageViewDrawable

        DrawableRecycler().recycleImageView(mockImageView)
        verify { VectorDrawablePool.put(mockImageViewDrawable) }
    }
}