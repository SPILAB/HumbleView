package spilab.net.humbleimageview.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.common.MockHumbleImageView
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.features.sizelist.UrlsWithSizes
import spilab.net.humbleimageview.features.sizelist.UrlWithSize
import spilab.net.humbleimageview.view.ViewSize

internal class HumbleImageFeaturesTest {

    private lateinit var mockHumbleViewModel: HumbleViewRequest
    private lateinit var spyHumbleViewRequest: HumbleViewRequest

    @Before
    fun beforeTests() {
        mockHumbleViewModel = mockk(relaxed = true)
        spyHumbleViewRequest = spyk(mockHumbleViewModel, recordPrivateCalls = true)
    }

    @Test
    fun `Given a default humble view, When the configuration change, Then configure current and next images view drawable`() {
        val mockHumbleImageView = MockHumbleImageView()
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage, spyHumbleViewRequest)
        humbleImageFeatures.configureFromImageView()
        verify { mockHumbleImageView.imageViewDrawableCurrent.configureFromImageView() }
        verify { mockHumbleImageView.imageViewDrawableNext.configureFromImageView() }
    }

    @Test
    fun `Given a humble view with vector drawable from resource, When the configuration change, Then configure the vector drawable size`() {
        val mockDrawableFromResId = mockk<VectorDrawableFromResId>(relaxed = true)
        val mockHumbleImageView = MockHumbleImageView(imageViewDrawable = mockDrawableFromResId)
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage, spyHumbleViewRequest)
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
                spyHumbleViewRequest, mockFeatureTransition)
        humbleImageFeatures.drawableReplaced()
        verify { mockFeatureTransition.drawableReplaced() }
    }

    @Test
    fun `Given a humble view, When set an url, Then should update the request url`() {
        val mockHumbleImageView = MockHumbleImageView()
        val mockFeatureTransition = mockk<FeatureTransition>(relaxed = true)
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewRequest, mockFeatureTransition)
        humbleImageFeatures.setUrl("url")
        verify { spyHumbleViewRequest.urls = UrlsWithSizes.fromUrl("url") }
    }

    @Test
    fun `Given a humble view, When set urls, Then should update the request url`() {
        val mockHumbleImageView = MockHumbleImageView()
        val mockFeatureTransition = mockk<FeatureTransition>(relaxed = true)
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewRequest, mockFeatureTransition)
        val sizeList = UrlsWithSizes(listOf(UrlWithSize("url", ViewSize(123, 456))))
        humbleImageFeatures.setUrls(sizeList)
        verify { spyHumbleViewRequest.urls = sizeList }
    }
}