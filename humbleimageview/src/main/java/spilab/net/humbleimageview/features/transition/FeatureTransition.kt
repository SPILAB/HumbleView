package spilab.net.humbleimageview.features.transition

import android.support.v4.view.ViewCompat
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class FeatureTransition(private val imageViewDrawables: HumbleImageView,
                                 private val drawableRecycler: DrawableRecycler) : Transition.TransitionListener {

    private var humbleTransition: Transition? = null

    fun addTransition(drawable: HumbleBitmapDrawable) {
        if (ViewCompat.isAttachedToWindow(imageViewDrawables)) {
            humbleTransition = CrossFadeTransition(imageViewDrawables,
                    imageViewDrawables, drawable, this)
        }
    }

    override fun onTransitionCompleted() {
        humbleTransition = null
        drawableRecycler.recycleImageViewDrawable(imageViewDrawables.imageViewDrawables[HumbleImageView.NEXT_IDX])
    }

    fun prepareOnDraw() {
        humbleTransition?.prepareOnDraw()
    }

    fun onPause() {
        cancelCurrentTransition()
        if (imageViewDrawables.imageViewDrawables[HumbleImageView.CURRENT_IDX].getDrawable() is HumbleBitmapDrawable) {
            humbleTransition = PaletteTransition(imageViewDrawables.imageViewDrawables)
        }
    }

    fun onDetached() {
        cancelCurrentTransition()
    }

    private inline fun cancelCurrentTransition() {
        humbleTransition?.cancel()
        humbleTransition = null
    }
}