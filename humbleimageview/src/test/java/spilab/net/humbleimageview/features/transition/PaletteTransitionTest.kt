package spilab.net.humbleimageview.features.transition

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.graphics.Palette
import io.mockk.*
import org.junit.Assert
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

class PaletteTransitionTest {

    @Test
    fun `Given an view with an humble bitmap drawable, When create a palette transition, Then replace the bitmap by a color drawable`() {

        val (mockBitmap, updatedDrawable, imageViewDrawables) = createImageViewDrawableMock()
        val mockAsyncTask = mockk<AsyncTask<Bitmap, Void, Palette>>()
        val mockListener = mockk<Transition.TransitionListener>(relaxed = true)
        val (mockAndroidPalette, paletteResultCallback, mockPalette) = createPaletteMock(mockBitmap, mockAsyncTask)
        val mockDrawableRecycler = mockk<DrawableRecycler>(relaxed = true)


        PaletteTransition(imageViewDrawables, mockListener, mockAndroidPalette, mockDrawableRecycler)
        paletteResultCallback.captured.invoke(mockPalette)
        Assert.assertTrue(updatedDrawable.captured is ColorDrawable)
        verify { mockDrawableRecycler.recycleImageViewDrawable(imageViewDrawables[0]) }
        verify { mockDrawableRecycler.recycleImageViewDrawable(imageViewDrawables[1]) }
        verify { mockListener.onTransitionCompleted() }
    }

    @Test
    fun `Given an view with an humble bitmap drawable, When cancel, Then cancel the task`() {
        val (mockBitmap, updatedDrawable, imageViewDrawables) = createImageViewDrawableMock()
        val mockAsyncTask = mockk<AsyncTask<Bitmap, Void, Palette>>(relaxed = true)
        val listener = mockk<Transition.TransitionListener>()
        val (mockAndroidPalette, paletteResultCallback, mockPalette) = createPaletteMock(mockBitmap, mockAsyncTask)

        PaletteTransition(imageViewDrawables, listener, mockAndroidPalette).cancel()

        verify { mockAsyncTask.cancel(true) }
    }

    private fun createImageViewDrawableMock(): Triple<Bitmap, CapturingSlot<Drawable>, Array<ImageViewDrawable>> {
        val mockBitmap = mockk<Bitmap>()

        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.bitmap } returns mockBitmap

        val mockImageViewDrawable = mockk<ImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val updatedDrawable = slot<Drawable>()
        every { mockImageViewDrawable.setDrawable(capture(updatedDrawable)) } answers {}

        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawable)
        return Triple(mockBitmap, updatedDrawable, imageViewDrawables)
    }

    private fun createPaletteMock(mockBitmap: Bitmap, mockAsyncTask: AsyncTask<Bitmap, Void, Palette>): Triple<AndroidPalette, CapturingSlot<(Palette) -> Unit>, Palette> {
        val mockAndroidPalette = mockk<AndroidPalette>()

        val mockPaletteSwatch = mockk<Palette.Swatch>()
        every { mockPaletteSwatch.rgb } returns Color.MAGENTA

        val mockPalette = mockk<Palette>()
        every { mockPalette.mutedSwatch } returns mockPaletteSwatch

        val paletteResultCallback = slot<(palette: Palette) -> Unit>()
        every {
            mockAndroidPalette.generate(mockBitmap, capture(paletteResultCallback))
        } returns mockAsyncTask
        return Triple(mockAndroidPalette, paletteResultCallback, mockPalette)
    }
}