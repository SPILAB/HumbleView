package spilab.net.humbleimageview.model

import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.view.ViewSize

class HumbleResourceIdTest {

    private lateinit var humbleResourceId: ResourceId

    @Before
    fun beforeTests() {
        humbleResourceId = ResourceId("urlWithSize", ViewSize(4, 16))
    }

    @Test
    fun `Given a resource id, When search in a list with null drawable, Then return false`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val imageViewDrawables = arrayOf(mockImageViewDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with another id, Then return false`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.resourceId } returns ResourceId("another urlWithSize", ViewSize(4, 16))
        val mockImageViewDrawableWithDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with the same id, Then return true`() {
        val mockImageViewDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.resourceId } returns ResourceId("urlWithSize", ViewSize(4, 16))
        val mockImageViewDrawableWithDrawable = mockk<AndroidImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertTrue(humbleResourceId.isPresentIn(imageViewDrawables))
    }
}