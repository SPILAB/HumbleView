package spilab.net.humbleimageview.model

import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

class HumbleResourceIdTest {

    private lateinit var humbleResourceId: HumbleResourceId

    @Before
    fun setUp() {
        humbleResourceId = HumbleResourceId("url", ViewSize(4, 16))
    }

    @Test
    fun `Given a resource id, When search in a list with null drawable, Then return false`() {
        val mockImageViewDrawable = mockk<ImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val imageViewDrawables = arrayOf(mockImageViewDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with another id, Then return false`() {
        val mockImageViewDrawable = mockk<ImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.humbleResourceId } returns HumbleResourceId("another url", ViewSize(4, 16))
        val mockImageViewDrawableWithDrawable = mockk<ImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertFalse(humbleResourceId.isPresentIn(imageViewDrawables))
    }

    @Test
    fun `Given a resource id, When search in a list with one humble bitmap with the same id, Then return true`() {
        val mockImageViewDrawable = mockk<ImageViewDrawable>()
        every { mockImageViewDrawable.getDrawable() } returns null
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        every { mockHumbleBitmapDrawable.humbleResourceId } returns HumbleResourceId("url", ViewSize(4, 16))
        val mockImageViewDrawableWithDrawable = mockk<ImageViewDrawable>()
        every { mockImageViewDrawableWithDrawable.getDrawable() } returns mockHumbleBitmapDrawable
        val imageViewDrawables = arrayOf(mockImageViewDrawable, mockImageViewDrawableWithDrawable)
        Assert.assertTrue(humbleResourceId.isPresentIn(imageViewDrawables))
    }
}