package spilab.net.humbleimageview.features.transition.scale

import android.widget.ImageView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert
import org.junit.Test

class ScaleImageViewDelegateTest {

    class ImageViewDelegateTest(scaleDelegate: ScaleDelegate) {
        var scale by scaleDelegate
    }

    @Test
    fun `Given an image view with a scale, When get scale, Then return the image view scale`() {
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ImageView.ScaleType.FIT_START
        Assert.assertEquals(ImageView.ScaleType.FIT_START,
                ImageViewDelegateTest(ScaleImageViewDelegate(mockImageView)).scale)
    }

    @Test
    fun `Given an image view, When set scale, Then set the image view drawable`() {
        val mockImageView = mockk<ImageView>(relaxed = true)
        ImageViewDelegateTest(ScaleImageViewDelegate(mockImageView)).scale = ImageView.ScaleType.FIT_END
        verify { mockImageView.scaleType = ImageView.ScaleType.FIT_END }
    }
}