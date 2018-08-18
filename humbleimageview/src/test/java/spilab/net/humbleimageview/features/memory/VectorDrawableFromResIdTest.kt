package spilab.net.humbleimageview.features.memory

import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.Test

class VectorDrawableFromResIdTest {

    @Test
    fun `Given a drawable from resource, When call any method on it, Then should forward to the drawable object`() {
        val mockDrawable = mockk<Drawable>(relaxed = true)
        val drawableFromResId = VectorDrawableFromResId(mockDrawable, 111, 222, 333)
        drawableFromResId.draw(null)
        drawableFromResId.alpha = 127
        drawableFromResId.opacity
        val mockColorFilter = mockk<ColorFilter>()
        drawableFromResId.colorFilter = mockColorFilter
        verifyAll {
            mockDrawable.draw(null)
            mockDrawable.alpha = 127
            mockDrawable.colorFilter = mockColorFilter
            mockDrawable.opacity
        }
    }
}