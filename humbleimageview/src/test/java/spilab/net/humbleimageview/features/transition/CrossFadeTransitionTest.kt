package spilab.net.humbleimageview.features.transition

import android.graphics.drawable.Drawable
import android.widget.ImageView
import io.mockk.*
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

class CrossFadeTransitionTest {

    private val mockImageView = mockk<ImageView>(relaxed = true)
    private val mockDrawableCurrent = mockk<Drawable>(relaxed = true)
    private val mockDrawableNext = mockk<Drawable>(relaxed = true)
    private val mockImageCurrent = mockk<AndroidImageViewDrawable>(relaxed = true)
    private val mockImageNext = mockk<AndroidImageViewDrawable>(relaxed = true)
    private val imageViewDrawables = arrayOf(mockImageCurrent, mockImageNext)
    private val mockDrawable = mockk<HumbleBitmapDrawable>()
    private val mockListener = mockk<Transition.TransitionListener>(relaxed = true)
    private val mockAnimationTimer = mockk<AnimationTimer>(relaxed = true)
    private val mockDrawableRecycler = mockk<DrawableRecycler>(relaxed = true)

    @Test
    fun `Given an cross fade, When the animation is half full, Then current and next drawable should be 50% opaque`() {
        every { mockImageCurrent.getDrawable() } returns mockDrawableCurrent
        every { mockImageNext.getDrawable() } returns mockDrawableNext

        every { mockImageView.imageAlpha } returns 255
        every { mockAnimationTimer.getNormalized(255.0f) } returns 127.0f

        val crossFadeTransition = CrossFadeTransition(mockImageView, imageViewDrawables, mockDrawable, mockListener, mockAnimationTimer, mockDrawableRecycler)
        crossFadeTransition.prepareOnDraw()
        verifySequence {
            mockDrawableCurrent.mutate()
            mockDrawableCurrent.alpha = 255
            mockDrawableCurrent.alpha = 128
        }
        verifySequence {
            mockDrawableNext.mutate()
            mockDrawableNext.alpha = 0
            mockDrawableNext.alpha = 127
        }
    }

    @Test
    fun `Given an cross fade, When the animation is finish, Then should recycle the place holder`() {
        every { mockImageCurrent.getDrawable() } returns mockDrawableCurrent
        every { mockImageNext.getDrawable() } returns mockDrawableNext

        every { mockImageView.imageAlpha } returns 255
        every { mockAnimationTimer.getNormalized(255.0f) } returns 255.0f

        val slotRunnable = slot<Runnable>()
        every { mockImageView.postOnAnimation(capture(slotRunnable)) } answers {}

        val crossFadeTransition = CrossFadeTransition(mockImageView, imageViewDrawables, mockDrawable, mockListener, mockAnimationTimer, mockDrawableRecycler)
        crossFadeTransition.prepareOnDraw()
        slotRunnable.captured.run()
        verifySequence {
            mockDrawableCurrent.mutate()
            mockDrawableCurrent.alpha = 255
            mockDrawableCurrent.alpha = 0
            mockDrawableCurrent.alpha = 255
        }
        verifySequence {
            mockDrawableNext.mutate()
            mockDrawableNext.alpha = 0
            mockDrawableNext.alpha = 255
        }
        verify { mockDrawableRecycler.recycleImageView(mockImageView) }
        verify { mockListener.onTransitionCompleted() }
    }

    @Test
    fun `Given an cross fade, When the drawable is replaced, Then should stop the current cross fading`() {
        every { mockImageCurrent.getDrawable() } returns mockDrawableCurrent
        every { mockImageNext.getDrawable() } returns mockDrawableNext

        every { mockImageView.imageAlpha } returns 255
        every { mockAnimationTimer.getNormalized(255.0f) } returns 127.0f

        val crossFadeTransition = CrossFadeTransition(mockImageView, imageViewDrawables, mockDrawable, mockListener, mockAnimationTimer, mockDrawableRecycler)
        crossFadeTransition.drawableReplaced()
        verify { mockDrawableCurrent.alpha = 255 }
        verify { mockDrawableNext.alpha = 0 }
        verify { mockImageNext.setDrawable(null) }
        verify { mockListener.onTransitionCompleted() }
    }
}