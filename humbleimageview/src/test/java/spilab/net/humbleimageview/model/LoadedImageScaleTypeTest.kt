package spilab.net.humbleimageview.model

import android.content.res.TypedArray
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import spilab.net.humbleimageview.R
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

class LoadedImageScaleTypeTest {

    @Test
    fun `Given unset loaded scale type, When get scale type, Then return the image view scale type`() {
        val loadedImageScaleType = createLoadedImageScaleType(LoadedImageScaleType.SCALE_TYPE_UNSET)
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ScaleType.FIT_CENTER
        val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
        Assert.assertEquals(ScaleType.FIT_CENTER,
                loadedImageScaleType.getScaleType(mockImageView, mockHumbleBitmapDrawable))
    }

    @Test
    fun `Given loaded scale type set from styled attribute, When get scale type, Then return styled attribute scale type`() {
        val scaleTypeMap = createScaleTypeMap()
        scaleTypeMap.forEach { styledValue, expectedScaleType ->
            val loadedImageScaleType = createLoadedImageScaleType(styledValue)
            val mockImageView = mockk<ImageView>()
            val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
            Assert.assertEquals(expectedScaleType,
                    loadedImageScaleType.getScaleType(mockImageView, mockHumbleBitmapDrawable))
        }
    }

    @Test
    fun `Given loaded scale type set from styled attribute, When get scale type for not an HumbleBitmapDrawable, Then return the image view scale type`() {
        val loadedImageScaleType = createLoadedImageScaleType(0)
        val mockImageView = mockk<ImageView>()
        every { mockImageView.scaleType } returns ScaleType.FIT_CENTER
        val mockBitmapDrawable = mockk<BitmapDrawable>()
        Assert.assertEquals(ScaleType.FIT_CENTER,
                loadedImageScaleType.getScaleType(mockImageView, mockBitmapDrawable))
    }

    @Test
    fun `Given loaded scale type set from code, When get scale type, Then return styled attribute scale type`() {
        val scaleTypeList = createScaleTypeList()
        scaleTypeList.forEach {
            val loadedImageScaleType = createLoadedImageScaleType(LoadedImageScaleType.SCALE_TYPE_UNSET)
            loadedImageScaleType.setLoadedImageScaleType(it)
            val mockImageView = mockk<ImageView>()
            val mockHumbleBitmapDrawable = mockk<HumbleBitmapDrawable>()
            Assert.assertEquals(it,
                    loadedImageScaleType.getScaleType(mockImageView, mockHumbleBitmapDrawable))
        }
    }

    private fun createLoadedImageScaleType(scaleType: Int): LoadedImageScaleType {
        val styledAttributes = mockk<TypedArray>()
        every { styledAttributes.getInteger(R.styleable.HumbleImageView_loadedImageScaleType, LoadedImageScaleType.SCALE_TYPE_UNSET) } returns scaleType
        return LoadedImageScaleType(styledAttributes)
    }

    private fun createScaleTypeMap(): Map<Int, ScaleType> {
        return mapOf(
                0 to ImageView.ScaleType.MATRIX,
                1 to ImageView.ScaleType.FIT_XY,
                2 to ImageView.ScaleType.FIT_START,
                3 to ImageView.ScaleType.FIT_CENTER,
                4 to ImageView.ScaleType.FIT_END,
                5 to ImageView.ScaleType.CENTER,
                6 to ImageView.ScaleType.CENTER_CROP,
                7 to ImageView.ScaleType.CENTER_INSIDE)
    }

    private fun createScaleTypeList(): List<ScaleType> {
        return listOf(ImageView.ScaleType.MATRIX, ImageView.ScaleType.FIT_XY,
                ImageView.ScaleType.FIT_START, ImageView.ScaleType.FIT_CENTER,
                ImageView.ScaleType.FIT_END, ImageView.ScaleType.CENTER,
                ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE)
    }
}