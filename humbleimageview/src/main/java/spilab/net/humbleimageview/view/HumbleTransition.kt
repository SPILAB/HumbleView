package spilab.net.humbleimageview.view

import android.graphics.drawable.Drawable
import android.os.SystemClock
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleTransition(private val humbleViewImage: HumbleImageView,
                                private val imageViewDrawables: Array<ImageViewDrawable>, drawable: HumbleBitmapDrawable) : Runnable {
    companion object {
        var DEFAULT_FADING_TIME_MILLIS = 1500L
        const val CURRENT_IDX = 0
        const val NEXT_IDX = 1
    }

    private val maxAlpha: Int
    private var fadingAlpha: Int = 0

    private val fadingAnimationTimer: AnimationTimer

    init {
        imageViewDrawables[NEXT_IDX].mDrawable = drawable
        imageViewDrawables[CURRENT_IDX].mDrawable?.mutate()
        imageViewDrawables[NEXT_IDX].mDrawable?.mutate()
        fadingAnimationTimer = AnimationTimer(DEFAULT_FADING_TIME_MILLIS, { SystemClock.uptimeMillis() })
        maxAlpha = humbleViewImage.imageAlpha
    }

    fun start() {
        fadingAnimationTimer.start()
        animationLoop()
    }

    inline fun setupAlpha() {
        if (!isCompleted()) {
            fadingAlpha = (fadingAnimationTimer.getNormalized(maxAlpha.toFloat())).toInt()
            imageViewDrawables[CURRENT_IDX].mDrawable?.alpha = maxAlpha - fadingAlpha
            imageViewDrawables[NEXT_IDX].mDrawable?.alpha = fadingAlpha
        }
    }

    fun completeAnimationBySwitchingDrawable(): Drawable? {
        val recyclable = imageViewDrawables[CURRENT_IDX].mDrawable
        imageViewDrawables[CURRENT_IDX].mDrawable = null
        imageViewDrawables[NEXT_IDX].mDrawable?.alpha = maxAlpha
        humbleViewImage.setImageDrawable(imageViewDrawables[NEXT_IDX].mDrawable)
        imageViewDrawables[NEXT_IDX].mDrawable = null
        return recyclable
    }

    private fun animationLoop() {
        humbleViewImage.postOnAnimation(this)
    }

    override fun run() {
        if (!isCompleted()) {
            animationLoop()
            humbleViewImage.postInvalidate()
        } else {
            humbleViewImage.completeAnimation()
        }
    }

    private inline fun isCompleted(): Boolean = fadingAlpha == maxAlpha
}