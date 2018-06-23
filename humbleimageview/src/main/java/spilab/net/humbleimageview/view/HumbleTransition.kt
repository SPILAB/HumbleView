package spilab.net.humbleimageview.view

import android.os.SystemClock
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleTransition(private val imageViewDrawables: Array<ImageViewDrawable>,
                                drawable: HumbleBitmapDrawable,
                                private val humbleViewImage: HumbleImageView,
                                private val humbleTransitionListener: HumbleTransitionListener) : Runnable {

    companion object {
        const val CURRENT_IDX = 0
        const val NEXT_IDX = 1
    }

    interface HumbleTransitionListener {
        fun onTansitionCompleted()
    }

    private val maxAlpha: Int
    private var fadingAlpha: Int = 0

    private val fadingAnimationTimer: AnimationTimer

    init {
        imageViewDrawables[NEXT_IDX].mDrawable = drawable
        imageViewDrawables[NEXT_IDX].mDrawable?.mutate()
        imageViewDrawables[CURRENT_IDX].mDrawable?.mutate()
        fadingAnimationTimer = AnimationTimer(HumbleViewAPI.fadingSpeedMillis, { SystemClock.uptimeMillis() })
        maxAlpha = humbleViewImage.imageAlpha
        imageViewDrawables[CURRENT_IDX].mDrawable?.alpha = maxAlpha
        imageViewDrawables[NEXT_IDX].mDrawable?.alpha = 0
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

    fun completeAnimation() {
        imageViewDrawables[CURRENT_IDX].mDrawable = imageViewDrawables[NEXT_IDX].mDrawable
        imageViewDrawables[CURRENT_IDX].mDrawable?.alpha = maxAlpha
        imageViewDrawables[NEXT_IDX].mDrawable = null
        humbleTransitionListener.onTansitionCompleted()
    }

    private fun animationLoop() {
        humbleViewImage.postOnAnimation(this)
    }

    override fun run() {
        if (!isCompleted()) {
            animationLoop()
            humbleViewImage.postInvalidate()
        } else {
            completeAnimation()
        }
    }

    private inline fun isCompleted(): Boolean = fadingAlpha == maxAlpha
}