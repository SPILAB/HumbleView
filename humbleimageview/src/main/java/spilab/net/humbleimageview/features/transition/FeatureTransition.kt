package spilab.net.humbleimageview.features.transition

import androidx.core.view.ViewCompat
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import java.util.*

internal class FeatureTransition(private val humbleImageView: HumbleImageView,
                                 private var transitions: MutableList<Transition> = mutableListOf(),
                                 private val androidPalette: AndroidPalette = AndroidPalette()) : Observable(), TransitionListener {

    enum class State {
        ON_TRANSITION_COMPLETED
    }

    fun addTransition(drawable: HumbleBitmapDrawable) {
        if (ViewCompat.isAttachedToWindow(humbleImageView)) {
            transitions.add(CrossFadeTransition(humbleImageView,
                    humbleImageView.imageViewDrawables,
                    drawable,
                    this))
        }
    }

    fun isCompleted(): Boolean {
        return transitions.isEmpty()
    }

    override fun onTransitionCompleted() {
        transitions.clear()
        setChanged()
        notifyObservers(State.ON_TRANSITION_COMPLETED)
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

    fun drawableReplaced() {
        if (transitions.isNotEmpty()) {
            transitions.last().drawableReplaced()
        }
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