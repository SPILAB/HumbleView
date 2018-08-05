package spilab.net.humbleimageview.features.transition

import android.os.SystemClock
import android.widget.ImageView
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.LoadedImageScaleType
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class CrossFadeTransition(private val imageView: ImageView,
                                   private val humbleImageView: HumbleImageView,
                                   drawable: HumbleBitmapDrawable,
                                   private val loadedImageScaleType: LoadedImageScaleType,
                                   private val transitionListener: Transition.TransitionListener) : Runnable, Transition {

    private val maxAlpha: Int
    private var fadingAlpha: Int = 0

    private var fadingAnimationTimer: AnimationTimer? = null

    init {
        humbleImageView.imageViewDrawables[Transition.NEXT_IDX].setDrawable(drawable, loadedImageScaleType.getScaleType(imageView, drawable))
        humbleImageView.imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.mutate()
        humbleImageView.imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.mutate()
        maxAlpha = imageView.imageAlpha
        humbleImageView.imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha
        humbleImageView.imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.alpha = 0
        animationLoop()
    }

    override fun prepareOnDraw() {
        startIfNeeded()
        setupAlpha()
    }

    override fun cancel() {
        finish()
    }

    private fun animationLoop() {
        imageView.postOnAnimation(this)
    }

    override fun run() {
        if (!isCompleted()) {
            animationLoop()
            imageView.postInvalidate()
        } else {
            finish()
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
            humbleImageView.imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha - fadingAlpha
            humbleImageView.imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.alpha = fadingAlpha
        }
    }

    private inline fun isCompleted(): Boolean = fadingAlpha == maxAlpha


    private fun finish() {
        humbleImageView.imageViewDrawables[Transition.CURRENT_IDX].setDrawable(humbleImageView.imageViewDrawables[Transition.NEXT_IDX].getDrawable(),
                loadedImageScaleType.getScaleType(imageView, humbleImageView.imageViewDrawables[Transition.NEXT_IDX].getDrawable()))
        humbleImageView.imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha
        humbleImageView.imageViewDrawables[Transition.NEXT_IDX].setDrawable(null, ImageViewDrawable.DEFAUL_SCALE_TYPE)
        transitionListener.onTransitionCompleted()
    }
}