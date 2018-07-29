package spilab.net.humbleimageview.features

import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.features.HumbleImageFeatures
import spilab.net.humbleimageview.model.HumbleViewModel

internal class HumbleImageFeaturesTest {

    private lateinit var humbleImageFeatures: HumbleImageFeatures
    private var debug = false

    @Before
    fun setUp() {
        val mockHumbleViewImage = mockk<HumbleImageView>()
        val mockHumbleViewModel = mockk<HumbleViewModel>(relaxed = true)
        val spyHumbleViewModel = spyk(mockHumbleViewModel, recordPrivateCalls = true)
        humbleImageFeatures = HumbleImageFeatures(mockHumbleViewImage, spyHumbleViewModel)
    }

    @Test
    fun `Given not set debug mode, When get debug mode, Then return false`() {
        Assert.assertFalse(humbleImageFeatures.getDebug())
    }

    @Test
    fun `Given set debug mode, When get debug mode, Then the set value`() {
        humbleImageFeatures.setDebug(true)
        Assert.assertTrue(humbleImageFeatures.getDebug())
    }
}