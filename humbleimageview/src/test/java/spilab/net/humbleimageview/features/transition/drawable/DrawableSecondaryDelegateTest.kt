package spilab.net.humbleimageview.features.transition.drawable

import android.graphics.drawable.Drawable
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class DrawableSecondaryDelegateTest {

    class ImageViewDelegateTest(drawableDelegate: DrawableDelegate) {
        var drawable by drawableDelegate
    }

    @Test
    fun `Given a unset delegate, When get drawable, Then should return null`() {
        val delegate = ImageViewDelegateTest(DrawableSecondaryDelegate())
        Assert.assertNull(delegate.drawable)
    }

    @Test
    fun `Given a delegate set with a drawable, When get drawable, Then should return the same drawable`() {
        val mockDrawable = mockk<Drawable>()
        val delegate = ImageViewDelegateTest(DrawableSecondaryDelegate())
        delegate.drawable = mockDrawable
        Assert.assertEquals(mockDrawable, delegate.drawable)
    }
}