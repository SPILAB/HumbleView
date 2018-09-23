package spilab.net.humbleimageview.features.transition.scale

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.widget.ImageView
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import spilab.net.humbleimageview.R
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import kotlin.reflect.KProperty

class ScaleDelegateTest {

    @Test
    fun `Given an image view, When set scale, Then set the image view drawable`() {
        val mockImageView = mockk<ImageView>(relaxed = true)
        createMockAndroidImageViewDrawable(mockImageView).setScaleType(ImageView.ScaleType.FIT_END)
        verify { mockImageView.scaleType = ImageView.ScaleType.FIT_END }
    }

    @Test
    fun `Given an unset loaded scale type and a humble drawable, When get scale type, Then return the image view scale type`() {
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ImageView.ScaleType.FIT_START
        every { mockImageView.drawable } returns mockk<HumbleBitmapDrawable>()
        Assert.assertEquals(ImageView.ScaleType.FIT_START,
                createMockAndroidImageViewDrawable(mockImageView).getScaleType())
    }

    @Test
    fun `Given loaded scale type set from styled attribute and a humble drawable, When get scale type, Then return styled attribute scale type`() {
        val mockImageView = mockk<ImageView>()
        val scaleDelegate = ScaleDelegate(mockImageView)
        scaleDelegate.initLoadedScaleType(createMockStyledAttributes(7))
        Assert.assertEquals(ImageView.ScaleType.CENTER_INSIDE,
                createMockAndroidImageViewDrawable(mockImageView, scaleDelegate = scaleDelegate).getScaleType())
    }

    @Test
    fun `Given loaded scale type set from code and a humble drawable, When get scale type, Then return loaded scale type`() {
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ImageView.ScaleType.FIT_START
        val scaleDelegate = ScaleDelegate(mockImageView)
        scaleDelegate.setLoadedScaleType(ImageView.ScaleType.FIT_XY)
        val imageViewDelegate = createMockAndroidImageViewDrawable(mockImageView, scaleDelegate = scaleDelegate)
        Assert.assertEquals(ImageView.ScaleType.FIT_XY, imageViewDelegate.getScaleType())
    }

    @Test
    fun `Given loaded scale type set from code without humble drawable, When get scale type, Then return image view scale type`() {
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ImageView.ScaleType.FIT_START
        val scaleDelegate = ScaleDelegate(mockImageView)
        scaleDelegate.setLoadedScaleType(ImageView.ScaleType.FIT_XY)
        val imageViewDelegate = createMockAndroidImageViewDrawable(mockImageView, mockk<Drawable>(), scaleDelegate)
        Assert.assertEquals(ImageView.ScaleType.FIT_START, imageViewDelegate.getScaleType())
    }

    private fun createMockAndroidImageViewDrawable(imageView: ImageView,
                                                   drawable: Drawable = mockk<HumbleBitmapDrawable>(),
                                                   scaleDelegate: ScaleDelegate = ScaleDelegate(imageView)): AndroidImageViewDrawable {
        val mockAndroidImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockAndroidImageViewDrawable.getDrawable() } returns drawable
        every { mockAndroidImageViewDrawable.getScaleType() } answers {
            val sacleType = scaleDelegate.getValue(mockAndroidImageViewDrawable, mockk<KProperty<ImageView.ScaleType>>())
            sacleType
        }
        val slotScaleType = slot<ImageView.ScaleType>()
        every { mockAndroidImageViewDrawable.setScaleType(capture(slotScaleType)) } answers {
            scaleDelegate.setValue(mockAndroidImageViewDrawable, mockk<KProperty<ImageView.ScaleType>>(), slotScaleType.captured)
        }
        return mockAndroidImageViewDrawable
    }

    private fun createMockStyledAttributes(scaleType: Int): TypedArray {
        val mockStyledAttributes = mockk<TypedArray>()
        every { mockStyledAttributes.getInteger(R.styleable.HumbleImageView_loadedImageScaleType, ScaleDelegate.SCALE_TYPE_UNSET) } returns scaleType
        return mockStyledAttributes
    }
}