package spilab.net.humbleimageview.features.transition

import android.os.SystemClock
import android.widget.ImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.LoadedImageScaleType
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class CrossFadeTransition(private val imageView: ImageView,
                                   private val imageViewDrawables: Array<ImageViewDrawable>,
                                   drawable: HumbleBitmapDrawable,
                                   private val loadedImageScaleType: LoadedImageScaleType,
                                   private val humbleTransitionListener: HumbleTransitionListener) : Runnable {

    companion object {
        const val CURRENT_IDX = 0
        const val NEXT_IDX = 1
    }

    interface HumbleTransitionListener {
        fun onTransitionCompleted()
    }

    private val maxAlpha: Int
    private var fadingAlpha: Int = 0

    private var fadingAnimationTimer: AnimationTimer? = null

    init {
        imageViewDrawables[NEXT_IDX].setDrawable(drawable, loadedImageScaleType.getScaleType(imageView, drawable))
        imageViewDrawables[NEXT_IDX].getDrawable()?.mutate()
        imageViewDrawables[CURRENT_IDX].getDrawable()?.mutate()
        maxAlpha = imageView.imageAlpha
        imageViewDrawables[CURRENT_IDX].getDrawable()?.alpha = maxAlpha
        imageViewDrawables[NEXT_IDX].getDrawable()?.alpha = 0
        animationLoop()
    }

    fun prepareOnDraw() {
        startIfNeeded()
        setupAlpha()
    }

    fun completeAnimation() {
        imageViewDrawables[CURRENT_IDX].setDrawable(imageViewDrawables[NEXT_IDX].getDrawable(),
                loadedImageScaleType.getScaleType(imageView, imageViewDrawables[NEXT_IDX].getDrawable()))
        imageViewDrawables[CURRENT_IDX].getDrawable()?.alpha = maxAlpha
        imageViewDrawables[NEXT_IDX].setDrawable(null, ImageViewDrawable.DEFAUL_SCALE_TYPE)
        humbleTransitionListener.onTransitionCompleted()
    }

    private fun animationLoop() {
        imageView.postOnAnimation(this)
    }

    override fun run() {
        if (!isCompleted()) {
            animationLoop()
            imageView.postInvalidate()
        } else {
            completeAnimation()
        }
    }

    private inline fun startIfNeeded() {
        if (fadingAnimationTimer == null) {
            fadingAnimationTimer = AnimationTimer(HumbleViewAPI.fadingSpeedMillis) { SystemClock.uptimeMillis() }
        }
    }

    private inline fun setupAlpha() {
        if (!isCompleted()) {
            fadingAlpha = (fadingAnimationTimer!!.getNormalized(maxAlpha.toFloat())).toInt()
            imageViewDrawables[CURRENT_IDX].getDrawable()?.alpha = maxAlpha - fadingAlpha
            imageViewDrawables[NEXT_IDX].getDrawable()?.alpha = fadingAlpha
        }
    }

    private inline fun isCompleted(): Boolean = fadingAlpha == maxAlpha
}