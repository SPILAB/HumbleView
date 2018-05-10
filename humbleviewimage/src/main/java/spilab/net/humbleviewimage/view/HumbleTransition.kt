package spilab.net.humbleviewimage.view

import android.graphics.drawable.Drawable
import android.os.SystemClock
import spilab.net.humbleviewimage.HumbleViewImage
import spilab.net.humbleviewimage.model.HumbleBitmapDrawable

internal class HumbleTransition(val drawable: HumbleBitmapDrawable) {

    companion object {
        var DEFAULT_FADING_TIME_MILLIS = 1500L
    }

    private val fadingAnimationTimer = AnimationTimer(DEFAULT_FADING_TIME_MILLIS,
            { SystemClock.uptimeMillis() })
    private var savedBitmap: Drawable? = null
    private var fadingAlpha: Int = 0

    fun prepareCurrentDrawable(humbleViewImage: HumbleViewImage) {
        fadingAlpha = (fadingAnimationTimer.getNormalized(255.0f)).toInt()
        humbleViewImage.drawable?.mutate()
        humbleViewImage.drawable?.alpha = 255 - fadingAlpha
    }

    fun prepareNextDrawable(humbleViewImage: HumbleViewImage) {
        savedBitmap = humbleViewImage.drawable
        drawable.alpha = fadingAlpha
        humbleViewImage.setImageDrawable(drawable)
    }

    fun isAnimationCompleted(): Boolean {
        return fadingAlpha == 255
    }

    fun restoreCurrentDrawable(humbleViewImage: HumbleViewImage) {
        humbleViewImage.setImageDrawable(savedBitmap)
    }

    fun completeAnimationImmediately(humbleViewImage: HumbleViewImage) {
        drawable.alpha = 255
        humbleViewImage.setImageDrawable(drawable)
    }
}