package spilab.net.humbleimageview.features.transition

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import io.mockk.*
import org.junit.Assert
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.memory.DrawableRecycler

class PaletteTransitionTest {

    @Test
    fun `Given an view with an humble bitmap drawable, When create and start a palette transition, Then replace the bitmap by a color drawable`() {

        val (mockBitmap, updatedDrawable, imageViewDrawables) = createImageViewDrawableMock()
        val mockAsyncTask = mockk<AsyncTask<Bitmap, Void, androidx.palette.graphics.Palette>>()
        val mockListener = mockk<TransitionListener>(relaxed = true)
        val (mockAndroidPalette, paletteResultCallback, mockPalette) = createPaletteMock(mockBitmap, mockAsyncTask)
        val mockDrawableRecycler = mockk<DrawableRecycler>(relaxed = true)


        val paletteTransition = PaletteTransition(imageViewDrawables, mockListener, mockAndroidPalette, mockDrawableRecycler)
        paletteTransition.start()

        paletteResultCallback.captured.invoke(mockPalette)
        Assert.assertTrue(updatedDrawable.captured is ColorDrawable)
        verify { mockDrawableRecycler.recycleImageViewDrawable(imageViewDrawables[0]) }
        verify { mockDrawableRecycler.recycleImageViewDrawable(imageViewDrawables[1]) }
        verify { mockListener.onTransitionCompleted() }
    }

    @Test
    fun `Given an view with an humble bitmap drawable, When detaching, Then cancel the task`() {
        val (mockBitmap, updatedDrawable, imageViewDrawables) = createImageViewDrawableMock()
        val mockAsyncTask = mockk<AsyncTask<Bitmap, Void, androidx.palette.graphics.Palette>>(relaxed = true)
        val mockListener = mockk<TransitionListener>(relaxed = true)
        val (mockAndroidPalette, paletteResultCallback, mockPalette) = createPaletteMock(mockBitmap, mockAsyncTask)

        val paletteTransition = PaletteTransition(imageViewDrawables, mockListener, mockAndroidPalette)
        paletteTransition.start()
        paletteTransition.onDetached()

        verify { mockAsyncTask.cancel(true) }
        verify { mockListener.onTransitionCompleted() }
    }

    @Test
    fun `Given a detached view with an humble bitmap drawable, When attached and detaching, Then should cancel the task to keep the current bitmap`() {
        val (mockBitmap, updatedDrawable, imageViewDrawables) = createImageViewDrawableMock()
        val mockAsyncTask = mockk<AsyncTask<Bitmap, Void, androidx.palette.graphics.Palette>>(relaxed = true)
        val mockListener = mockk<TransitionListener>(relaxed = true)
        val (mockAndroidPalette, paletteResultCallback, mockPalette) = createPaletteMock(mockBitmap, mockAsyncTask)

        val paletteTransition = PaletteTransition(imageViewDrawables, mockListener, mockAndroidPalette)
        paletteTransition.start()

        paletteTransition.onDetached()
        paletteTransition.onAttached()

        verify { mockAsyncTask.cancel(true) }
        verify { mockListener.onTransitionCompleted() }
    }

    @Test
    fun `Given an view with an humble bitmap drawable, When drawable is replaced, Then cancel the task`() {
        val (mockBitmap, updatedDrawable, imageViewDrawables) = createImageViewDrawableMock()
        val mockAsyncTask = mockk<AsyncTask<Bitmap, Void, androidx.palette.graphics.Palette>>(relaxed = true)
        val mockListener = mockk<TransitionListener>(relaxed = true)
        val (mockAndroidPalette, paletteResultCallback, mockPalette) = createPaletteMock(mockBitmap, mockAsyncTask)

        val paletteTransition = PaletteTransition(imageViewDrawables, mockListener, mockAndroidPalette)
        paletteTransition.start()
        paletteTransition.cancel()

        verify { mockAsyncTask.cancel(true) }
        verify { mockListener.onTransitionCompleted() }
    }

    private fun createImageViewDrawableMock(): Triple<Bitmap, CapturingSlot<Drawable>, Array<AndroidImageViewDrawable>> {
        val mockBitmap = mockk<Bitmap>()

        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.bitmap } returns mockBitmap

        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val updatedDrawable = slot<Drawable>()
        every { mockImageViewDrawable.setDrawable(capture(updatedDrawable)) } answers {}

        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawable)
        return Triple(mockBitmap, updatedDrawable, imageViewDrawables)
    }

    private fun createPaletteMock(mockBitmap: Bitmap, mockAsyncTask: AsyncTask<Bitmap, Void, androidx.palette.graphics.Palette>): Triple<AndroidPalette, CapturingSlot<(androidx.palette.graphics.Palette) -> Unit>, androidx.palette.graphics.Palette> {
        val mockAndroidPalette = mockk<AndroidPalette>()

        val mockPaletteSwatch = mockk<androidx.palette.graphics.Palette.Swatch>()
        every { mockPaletteSwatch.rgb } returns Color.MAGENTA

        val mockPalette = mockk<androidx.palette.graphics.Palette>()
        every { mockPalette.mutedSwatch } returns mockPaletteSwatch

        val paletteResultCallback = slot<(palette: androidx.palette.graphics.Palette) -> Unit>()
        every {
            mockAndroidPalette.generate(mockBitmap, capture(paletteResultCallback))
        } returns mockAsyncTask
        return Triple(mockAndroidPalette, paletteResultCallback, mockPalette)
    }
}