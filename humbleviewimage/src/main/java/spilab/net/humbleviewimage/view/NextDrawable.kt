package spilab.net.humbleviewimage.view

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import spilab.net.humbleviewimage.HumbleViewImage

internal class NextDrawable(humbleViewImage: HumbleViewImage, bitmap: Bitmap) {

    companion object {
        var DEFAULT_FADING_TIME_MILLIS = 500L
    }

    private val fadingAnimationTimer = AnimationTimer(DEFAULT_FADING_TIME_MILLIS)
    private var drawable: BitmapDrawable = BitmapDrawable(humbleViewImage.resources, bitmap)
    private var savedBitmap: Drawable? = null
    private var fadingAlpha: Int = 0

    fun prepareNextDrawable(humbleViewImage: HumbleViewImage) {
        savedBitmap = humbleViewImage.drawable
        fadingAlpha = (fadingAnimationTimer.getNormalized(255.0f)).toInt()
        drawable.alpha = fadingAlpha
        humbleViewImage.setImageDrawable(drawable)
    }

    fun isAnimationCompleted(): Boolean {
        return fadingAlpha == 255
    }

    fun restoreCurrentDrawable(humbleViewImage: HumbleViewImage) {
        humbleViewImage.setImageDrawable(savedBitmap)
    }

}