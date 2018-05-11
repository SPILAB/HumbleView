package spilab.net.humbleviewimage.view

import android.graphics.Canvas
import android.os.SystemClock
import spilab.net.humbleviewimage.HumbleViewImage
import spilab.net.humbleviewimage.android.ImageViewDrawable
import spilab.net.humbleviewimage.model.HumbleBitmapDrawable
import spilab.net.humbleviewimage.model.HumbleBitmapId

internal class HumbleTransition(private val humbleViewImage: HumbleViewImage, private val drawable: HumbleBitmapDrawable) {

    companion object {
        var DEFAULT_FADING_TIME_MILLIS = 1500L
    }

    private val fadingAnimationTimer: AnimationTimer
    private var fadingAlpha: Int = 0
    private var imageViewDrawable: ImageViewDrawable

    init {
        this.fadingAnimationTimer = AnimationTimer(DEFAULT_FADING_TIME_MILLIS,
                { SystemClock.uptimeMillis() })
        imageViewDrawable = ImageViewDrawable(humbleViewImage, drawable)
    }

    fun isBitmapId(bitmapId: HumbleBitmapId): Boolean = (drawable.humbleBitmapId == bitmapId)

    fun start() {
        fadingAnimationTimer.start()
        animationLoop()
    }

    private fun animationLoop() {
        humbleViewImage.postOnAnimation {
            humbleViewImage.postInvalidate()
            if (!isCompleted()) {
                animationLoop()
            }
            else {
                humbleViewImage.completeAnimation()
            }
        }
    }

    fun completeAnimationBySwitchingDrawable() {
        drawable.alpha = 255
        humbleViewImage.setImageDrawable(drawable)
    }

    inline fun prepareFading() {
        fadingAlpha = (fadingAnimationTimer.getNormalized(255.0f)).toInt()
        humbleViewImage.drawable?.mutate()
        humbleViewImage.drawable?.alpha = 255 - fadingAlpha
    }

    inline fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            imageViewDrawable.mDrawable.mutate()
            imageViewDrawable.mDrawable.alpha = fadingAlpha
            imageViewDrawable.onDraw(canvas)
        }
    }

    private inline fun isCompleted(): Boolean = fadingAlpha == 255
}