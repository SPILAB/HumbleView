package spilab.net.humbleimageview.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.common.MockHumbleImageView
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.model.HumbleViewModel

internal class HumbleImageFeaturesTest {

    private lateinit var mockHumbleViewModel: HumbleViewModel
    private lateinit var spyHumbleViewModel: HumbleViewModel

    @Before
    fun beforeTests() {
        mockHumbleViewModel = mockk(relaxed = true)
        spyHumbleViewModel = spyk(mockHumbleViewModel, recordPrivateCalls = true)
    }

    @Test
    fun `Given not set debug mode, When get debug mode, Then return false`() {
        val humbleImageFeatures = HumbleImageFeatures(MockHumbleImageView().humbleViewImage, spyHumbleViewModel)
        Assert.assertFalse(humbleImageFeatures.getDebug())
    }

    @Test
    fun `Given set debug mode, When get debug mode, Then the set value`() {
        val humbleImageFeatures = HumbleImageFeatures(MockHumbleImageView().humbleViewImage, spyHumbleViewModel)
        humbleImageFeatures.setDebug(true)
        Assert.assertTrue(humbleImageFeatures.getDebug())
    }

    @Test
    fun `Given a default humble view, When the configuration change, Then configure current and next images view drawable`() {
        val mockHumbleImageView = MockHumbleImageView()
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage, spyHumbleViewModel)
        humbleImageFeatures.configureFromImageView()
        verify { mockHumbleImageView.imageViewDrawableCurrent.configureFromImageView() }
        verify { mockHumbleImageView.imageViewDrawableNext.configureFromImageView() }
    }

    @Test
    fun `Given a humble view with vector drawable from resource, When the configuration change, Then configure the vector drawable size`() {
        val mockDrawableFromResId = mockk<VectorDrawableFromResId>(relaxed = true)
        val mockHumbleImageView = MockHumbleImageView(imageViewDrawable = mockDrawableFromResId)
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage, spyHumbleViewModel)
        every { mockHumbleImageView.humbleViewImage.width } returns 123
        every { mockHumbleImageView.humbleViewImage.height } returns 456
        humbleImageFeatures.configureFromImageView()
        verify { mockDrawableFromResId.width = 123 }
        verify { mockDrawableFromResId.height = 456 }
    }

    @Test
    fun `Given a humble view, When replaced drawable, Then should notify the transition`() {
        val mockHumbleImageView = MockHumbleImageView()
        val mockFeatureTransition = mockk<FeatureTransition>(relaxed = true)
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewModel, mockFeatureTransition)
        humbleImageFeatures.drawableReplaced()
        verify { mockFeatureTransition.drawableReplaced() }
    }
}