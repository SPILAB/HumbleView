package spilab.net.humbleimageview.features.transition

import android.support.v4.view.ViewCompat
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class FeatureTransition(private val humbleImageView: HumbleImageView,
                                 private var transitions: MutableList<Transition> = mutableListOf(),
                                 private val androidPalette: AndroidPalette = AndroidPalette()) : Transition.TransitionListener {

    fun addTransition(drawable: HumbleBitmapDrawable) {
        if (ViewCompat.isAttachedToWindow(humbleImageView)) {
            transitions.add(CrossFadeTransition(humbleImageView,
                    humbleImageView.imageViewDrawables,
                    drawable,
                    this))
        }
    }

    override fun onTransitionCompleted() {
        transitions.clear()
    }

    fun prepareOnDraw() {
        if (transitions.isNotEmpty()) {
            transitions.last().prepareOnDraw()
        }
    }

    fun onPause() {
        cancelCurrentTransition()
        replaceBitmapWithColor()
    }

    fun onAttached() {
        if (transitions.isNotEmpty()) {
            transitions.last().onAttached()
        }
    }

    fun onDetached() {
        cancelCurrentTransition()
        replaceBitmapWithColor()
    }

    private inline fun cancelCurrentTransition() {
        if (transitions.isNotEmpty()) {
            transitions.last().onDetached()
        }
        transitions.clear()
    }

    private fun replaceBitmapWithColor() {
        if (humbleImageView.imageViewDrawables[Transition.CURRENT_IDX].getDrawable() is HumbleBitmapDrawable) {
            transitions.add(PaletteTransition(humbleImageView.imageViewDrawables, this, androidPalette))
        }
    }
}