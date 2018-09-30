package spilab.net.humbleimageview.model

import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.sizelist.UrlWithSize
import spilab.net.humbleimageview.features.transform.DefaultTransformation
import spilab.net.humbleimageview.features.transform.RoundedBitmapTransformation
import spilab.net.humbleimageview.view.ViewSize

class HumbleResourceIdTest {

    private lateinit var humbleResourceId: ResourceId

    @Before
    fun beforeTests() {
        humbleResourceId = ResourceId(UrlWithSize("url", ViewSize(1, 2)), ViewSize(4, 16), DefaultTransformation())
    }

    @Test
    fun `Given a resource id, When search in a list with null drawable, Then return false`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val imageViewDrawables = arrayOf(mockImageViewDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with another url, Then return false`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.resourceId } returns ResourceId(UrlWithSize("another url", ViewSize(1, 2)), ViewSize(4, 16), DefaultTransformation())
        val mockImageViewDrawableWithDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with another url view size, Then return false`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.resourceId } returns ResourceId(UrlWithSize("url", ViewSize(2, 2)), ViewSize(4, 16), DefaultTransformation())
        val mockImageViewDrawableWithDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with another transformation, Then return false`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.resourceId } returns ResourceId(UrlWithSize("url", ViewSize(1, 2)), ViewSize(4, 16), RoundedBitmapTransformation())
        val mockImageViewDrawableWithDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with the same url view size and transformation, Then return true`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.resourceId } returns ResourceId(UrlWithSize("url", ViewSize(1, 2)), ViewSize(4, 16), DefaultTransformation())
        val mockImageViewDrawableWithDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertTrue(humbleResourceId.isPresentIn(imageViewDrawables))
    }
}