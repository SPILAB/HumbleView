package spilab.net.humbleimageview.features.transition

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert
import org.junit.Test
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

class FeatureTransitionTest {

    @Test
    fun `Given a view with a transition, When detached, Then should detach the current transition and set it to null`() {
        val mockHumbleImageView = mockk<HumbleImageView>(relaxed = true)
        val mockImageViewDrawables = arrayOf<ImageViewDrawable>(mockk(relaxed = true), mockk(relaxed = true))
        every { mockHumbleImageView.imageViewDrawables } returns mockImageViewDrawables
        val mockTransition = mockk<Transition>(relaxed = true)
        val transitions = mutableListOf(mockTransition)
        val featureTransition = FeatureTransition(mockHumbleImageView, transitions)
        featureTransition.onDetached()
        verify { mockTransition.onDetached() }
        Assert.assertTrue(transitions.isEmpty())
    }

    @Test
    fun `Given a view with a transition and a humble, When detached, Then should detach the current transition and create a palette transition`() {
        val mockHumbleImageView = mockk<HumbleImageView>(relaxed = true)
        val mockDrawable = mockk<HumbleBitmapDrawable>(relaxed = true)
        val mockImageViewDrawable = mockk<ImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns mockDrawable
        val mockImageViewDrawables = arrayOf(mockImageViewDrawable, mockk(relaxed = true))
        every { mockHumbleImageView.imageViewDrawables } returns mockImageViewDrawables
        val mockTransition = mockk<Transition>(relaxed = true)
        val transitions = mutableListOf(mockTransition)
        val mockPalette = mockk<AndroidPalette>(relaxed = true)
        val featureTransition = FeatureTransition(mockHumbleImageView, transitions, mockPalette)
        featureTransition.onDetached()
        verify { mockTransition.onDetached() }
        Assert.assertTrue(transitions[0] is PaletteTransition)
    }
}