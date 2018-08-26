package spilab.net.humbleimageview.common

import android.graphics.drawable.Drawable
import io.mockk.every
import io.mockk.mockk
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.AndroidImageViewDrawable

class MockHumbleImageView(drawableCurrent: Drawable = mockk(relaxed = true), drawableNext: Drawable = mockk(relaxed = true), imageViewDrawable: Drawable = mockk(relaxed = true)) {

    internal val imageViewDrawableCurrent = mockk<AndroidImageViewDrawable>(relaxed = true)
    internal val imageViewDrawableNext = mockk<AndroidImageViewDrawable>(relaxed = true)

    val humbleViewImage = mockk<HumbleImageView>()

    init {
        every { imageViewDrawableCurrent.getDrawable() } returns drawableCurrent
        every { imageViewDrawableNext.getDrawable() } returns drawableNext
        every { humbleViewImage.drawable } returns imageViewDrawable
        every { humbleViewImage.imageViewDrawables } returns
                arrayOf(imageViewDrawableCurrent, imageViewDrawableNext)
    }
}