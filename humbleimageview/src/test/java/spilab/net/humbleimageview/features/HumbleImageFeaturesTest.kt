package spilab.net.humbleimageview.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidViewCompat
import spilab.net.humbleimageview.common.MockHumbleImageView
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.features.sizelist.UrlWithSize
import spilab.net.humbleimageview.features.sizelist.UrlsWithSizes
import spilab.net.humbleimageview.features.slideshow.SlideshowFactory
import spilab.net.humbleimageview.features.slideshow.SlideshowUrls
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.view.ViewSize

internal class HumbleImageFeaturesTest {

    private var mockHumbleViewModel = mockk<HumbleViewRequest>(relaxed = true)
    private var mockFeatureTransition = mockk<FeatureTransition>(relaxed = true)
    private var mockHumbleImageView = MockHumbleImageView()
    private var mockSlideshowFactory = mockk<SlideshowFactory>(relaxed = true)
    private lateinit var spyHumbleViewRequest: HumbleViewRequest
    private var mockAndroidViewCompat = mockk<AndroidViewCompat>(relaxed = true)

    @Before
    fun beforeTests() {
        spyHumbleViewRequest = spyk(mockHumbleViewModel, recordPrivateCalls = true)
    }

    @Test
    fun `Given a default humble view, When the configuration change, Then configure current and next images view drawable`() {
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage, spyHumbleViewRequest)
        humbleImageFeatures.configureFromImageView()
        verify { mockHumbleImageView.imageViewDrawableCurrent.configureFromImageView() }
        verify { mockHumbleImageView.imageViewDrawableNext.configureFromImageView() }
    }

    @Test
    fun `Given a humble view with vector drawable from resource, When the configuration change, Then configure the vector drawable size`() {
        val mockDrawableFromResId = mockk<VectorDrawableFromResId>(relaxed = true)
        val mockHumbleImageViewWithMockedDrawable = MockHumbleImageView(imageViewDrawable = mockDrawableFromResId)
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageViewWithMockedDrawable.humbleViewImage, spyHumbleViewRequest)
        every { mockHumbleImageViewWithMockedDrawable.humbleViewImage.width } returns 123
        every { mockHumbleImageViewWithMockedDrawable.humbleViewImage.height } returns 456
        humbleImageFeatures.configureFromImageView()
        verify { mockDrawableFromResId.width = 123 }
        verify { mockDrawableFromResId.height = 456 }
    }

    @Test
    fun `Given a humble view, When replaced drawable, Then should notify the transition`() {
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewRequest, mockFeatureTransition)
        humbleImageFeatures.drawableReplaced()
        verify { mockFeatureTransition.drawableReplaced() }
    }

    @Test
    fun `Given a humble view, When set an url, Then should update the request url`() {
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewRequest, mockFeatureTransition)
        humbleImageFeatures.setUrl("url")
        verify { spyHumbleViewRequest.urls = UrlsWithSizes.fromUrl("url") }
    }

    @Test
    fun `Given a humble view, When set urls, Then should update the request url`() {
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewRequest, mockFeatureTransition)
        val sizeList = UrlsWithSizes(listOf(UrlWithSize("url", ViewSize(123, 456))))
        humbleImageFeatures.setUrls(sizeList)
        verify { spyHumbleViewRequest.urls = sizeList }
    }

    @Test
    fun `Given a humble view, When set slideshow urls, Then should create a slideshow`() {
        val slideshowUrls = arrayOf("urlA", "urlB", "urlC")
        val mockSlideshowUrls = mockk<SlideshowUrls>(relaxed = true)
        every { mockSlideshowFactory.fromUrls(spyHumbleViewRequest, mockFeatureTransition, false, slideshowUrls) } returns mockSlideshowUrls
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewRequest, mockFeatureTransition, mockSlideshowFactory, mockAndroidViewCompat)
        humbleImageFeatures.setSlideshowUrls(slideshowUrls)
        verify { mockSlideshowFactory.fromUrls(spyHumbleViewRequest, mockFeatureTransition, false, slideshowUrls) }
    }

    @Test
    fun `Given a humble view with a slideshow, When set url, Then should cancel the slideshow`() {
        // Given a humble view with a slideshow
        val slideshowUrls = arrayOf("urlA", "urlB", "urlC")
        val mockSlideshowUrls = mockk<SlideshowUrls>(relaxed = true)
        every { mockSlideshowFactory.fromUrls(spyHumbleViewRequest, mockFeatureTransition, false, slideshowUrls) } returns mockSlideshowUrls
        val humbleImageFeatures = HumbleImageFeatures(mockHumbleImageView.humbleViewImage,
                spyHumbleViewRequest, mockFeatureTransition, mockSlideshowFactory, mockAndroidViewCompat)
        humbleImageFeatures.setSlideshowUrls(slideshowUrls)
        verify { mockSlideshowFactory.fromUrls(spyHumbleViewRequest, mockFeatureTransition, false, slideshowUrls) }
        // When set url
        humbleImageFeatures.setUrl("url")
        // Then should cancel the slideshow
        verify { mockSlideshowUrls.cancel() }
    }
}