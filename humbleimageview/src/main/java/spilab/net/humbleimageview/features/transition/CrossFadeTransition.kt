package spilab.net.humbleimageview.features.transition

import android.os.SystemClock
import android.widget.ImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class CrossFadeTransition(private val imageView: ImageView,
                                   private val imageViewDrawables: Array<ImageViewDrawable>,
                                   drawable: HumbleBitmapDrawable,
                                   private val transitionListener: Transition.TransitionListener,
                                   private var fadingAnimationTimer: AnimationTimer = AnimationTimer(HumbleViewAPI.fadingSpeedMillis) { SystemClock.uptimeMillis() },
                                   private val drawableRecycler: DrawableRecycler = DrawableRecycler()) : Runnable, Transition {

    private val maxAlpha: Int
    private var fadingAlpha: Int = 0

    init {
        imageViewDrawables[Transition.NEXT_IDX].setDrawable(drawable)
        imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.mutate()
        imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.mutate()
        maxAlpha = imageView.imageAlpha
        imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha
        imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.alpha = 0
        animationLoop()
    }

    override fun prepareOnDraw() {
        fadingAnimationTimer.startIfNeeded()
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

    private inline fun setupAlpha() {
        if (!isCompleted()) {
            fadingAlpha = (fadingAnimationTimer!!.getNormalized(maxAlpha.toFloat())).toInt()
            imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha - fadingAlpha
            imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.alpha = fadingAlpha
        }
    }

    private inline fun isCompleted(): Boolean = fadingAlpha == maxAlpha


    private fun finish() {
        drawableRecycler.recycleImageViewDrawable(imageViewDrawables[Transition.CURRENT_IDX])
        imageViewDrawables[Transition.CURRENT_IDX].setDrawable(imageViewDrawables[Transition.NEXT_IDX].getDrawable())
        imageViewDrawables[Transition.CURRENT_IDX].setScaleType(imageViewDrawables[Transition.NEXT_IDX].getScaleType())
        imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha
        imageViewDrawables[Transition.NEXT_IDX].setDrawable(null)
        transitionListener.onTransitionCompleted()
    }
}