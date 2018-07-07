package spilab.net.humbleimageview.presenter

import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.model.HumbleViewModel

internal class HumbleViewPresenterTest {

    private lateinit var humbleViewPresenter: HumbleViewPresenter
    private var debug = false

    @Before
    fun setUp() {
        val mockHumbleViewImage = mockk<HumbleImageView>()
        val mockHumbleViewModel = mockk<HumbleViewModel>(relaxed = true)
        val spyHumbleViewModel = spyk(mockHumbleViewModel, recordPrivateCalls = true)
        humbleViewPresenter = HumbleViewPresenter(mockHumbleViewImage, spyHumbleViewModel)
    }

    @Test
    fun `Given not set debug mode, When get debug mode, Then return false`() {
        Assert.assertFalse(humbleViewPresenter.getDebug())
    }

    @Test
    fun `Given set debug mode, When get debug mode, Then the set value`() {
        humbleViewPresenter.setDebug(true)
        Assert.assertTrue(humbleViewPresenter.getDebug())
    }
}