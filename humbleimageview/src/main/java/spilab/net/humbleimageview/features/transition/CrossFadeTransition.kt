package spilab.net.humbleimageview.features.transition

import android.os.SystemClock
import android.widget.ImageView
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.memory.DrawableRecycler

internal class CrossFadeTransition(private val imageView: ImageView,
                                   private val imageViewDrawables: Array<AndroidImageViewDrawable>,
                                   private val drawable: HumbleBitmapDrawable,
                                   private val transitionListener: TransitionListener,
                                   private var fadingAnimationTimer: AnimationTimer = AnimationTimer(HumbleViewAPI.fadingSpeedMillis) { SystemClock.uptimeMillis() },
                                   private val drawableRecycler: DrawableRecycler = DrawableRecycler()) : Runnable, Transition {

    private var maxAlpha: Int = 0
    private var fadingAlpha: Int = -1

    override fun start() {
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

    override fun onAttached() {
        // Nothing to do.
    }

    override fun onDetached() {
        finish()
    }

    private fun animationLoop() {
        imageView.postOnAnimation(this)
    }

    override fun run() {
        if (!isCompleted()) {
            animationLoop()
            imageView.postInvalidateOnAnimation()
        } else {
            finish()
        }
    }

    private fun setupAlpha() {
        if (!isCompleted()) {
            fadingAlpha = (fadingAnimationTimer.getNormalized(maxAlpha.toFloat())).toInt()
            imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha - fadingAlpha
            imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.alpha = fadingAlpha
        }
    }

    private fun isCompleted(): Boolean = fadingAlpha == maxAlpha

    private fun finish() {
        drawableRecycler.recycleImageView(imageView)
        imageViewDrawables[Transition.CURRENT_IDX].setDrawable(imageViewDrawables[Transition.NEXT_IDX].getDrawable())
        fadingAlpha = maxAlpha
        imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = fadingAlpha
        imageViewDrawables[Transition.NEXT_IDX].setDrawable(null)
        transitionListener.onTransitionCompleted()
    }

    override fun cancel() {
        imageViewDrawables[Transition.CURRENT_IDX].getDrawable()?.alpha = maxAlpha
        imageViewDrawables[Transition.NEXT_IDX].getDrawable()?.alpha = 0
        imageViewDrawables[Transition.NEXT_IDX].setDrawable(null)
        fadingAlpha = maxAlpha
        transitionListener.onTransitionCompleted()
    }
}