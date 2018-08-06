package spilab.net.humbleimageview.features.transition.scale

import android.content.res.TypedArray
import android.widget.ImageView
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import spilab.net.humbleimageview.R

class ScaleSecondaryDelegateTest {

    /*  Attributes values
        <enum name="matrix" value="0" />
        <enum name="fitXY" value="1" />
        <enum name="fitStart" value="2" />
        <enum name="fitCenter" value="3" />
        <enum name="fitEnd" value="4" />
        <enum name="center" value="5" />
        <enum name="centerCrop" value="6" />
        <enum name="centerInside" value="7" />
     */


    class ImageViewDelegateTest(scaleDelegate: ScaleDelegate) {
        var scale by scaleDelegate
    }

    @Test
    fun `Given an unset loaded scale type, When get scale type, Then return the image view scale type`() {
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ImageView.ScaleType.FIT_START
        Assert.assertEquals(ImageView.ScaleType.FIT_START,
                ImageViewDelegateTest(ScaleSecondaryDelegate(mockImageView)).scale)
    }

    @Test
    fun `Given loaded scale type set from styled attribute, When get scale type, Then return styled attribute scale type`() {
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ImageView.ScaleType.FIT_START
        val scaleDelegate = ScaleSecondaryDelegate(mockImageView)
        scaleDelegate.initScaleType(createMockStyledAttributes(7))
        Assert.assertEquals(ImageView.ScaleType.CENTER_INSIDE,
                ImageViewDelegateTest(scaleDelegate).scale)
    }

    @Test
    fun `Given loaded scale type set from code, When get scale type, Then return styled attribute scale type`() {
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ImageView.ScaleType.FIT_START
        val imageViewDelegate = ImageViewDelegateTest(ScaleSecondaryDelegate(mockImageView))
        imageViewDelegate.scale = ImageView.ScaleType.FIT_XY
        Assert.assertEquals(ImageView.ScaleType.FIT_XY, imageViewDelegate.scale)
    }

    private fun createMockStyledAttributes(scaleType: Int): TypedArray {
        val mockStyledAttributes = mockk<TypedArray>()
        every { mockStyledAttributes.getInteger(R.styleable.HumbleImageView_loadedImageScaleType, ScaleSecondaryDelegate.SCALE_TYPE_UNSET) } returns scaleType
        return mockStyledAttributes
    }
}