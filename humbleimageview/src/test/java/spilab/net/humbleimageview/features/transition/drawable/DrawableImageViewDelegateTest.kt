package spilab.net.humbleimageview.features.transition.drawable

import android.graphics.drawable.Drawable
import android.widget.ImageView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class DrawableImageViewDelegateTest {

    class ImageViewDelegateTest(drawableDelegate: DrawableDelegate) {
        var drawable by drawableDelegate
    }

    @Test
    fun `Given an image view with a drawable, When get drawable, Then return the image view drawable`() {
        val mockDrawable = mockk<Drawable>()
        val mockImageView = mockk<ImageView>()
        every { mockImageView.drawable } returns mockDrawable
        Assert.assertEquals(
                mockDrawable,
                ImageViewDelegateTest(DrawableImageViewDelegate(mockImageView)).drawable)
    }

    @Test
    fun `Given an image view, When set drawable, Then set the image view drawable`() {
        val mockDrawable = mockk<Drawable>()
        val mockImageView = mockk<ImageView>(relaxed = true)
        ImageViewDelegateTest(DrawableImageViewDelegate(mockImageView)).drawable = mockDrawable
        verify { mockImageView.setImageDrawable(mockDrawable) }
    }
}