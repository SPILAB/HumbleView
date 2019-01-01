package spilab.net.humbleimageview.features.transition

import androidx.core.view.ViewCompat
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.log.HumbleLogs
import java.util.*

internal class FeatureTransition(private val humbleImageView: HumbleImageView,
                                 private var transitions: MutableList<Transition> = mutableListOf(),
                                 private val androidPalette: AndroidPalette = AndroidPalette()) : Observable(), TransitionListener {

    enum class State {
        ON_TRANSITION_COMPLETED
    }

    fun startCrossFadeTransitionFrom(drawable: HumbleBitmapDrawable) {
        if (ViewCompat.isAttachedToWindow(humbleImageView)) {
            val transition = CrossFadeTransition(humbleImageView,
                    humbleImageView.imageViewDrawables, drawable, this)
            addAndStartTransition(transition)
        }
    }

    fun isCompleted(): Boolean {
        return transitions.isEmpty()
    }

    override fun onTransitionCompleted() {
        HumbleLogs.log("FeatureTransition onTransitionCompleted with transitions = $transitions")
        if (transitions.isNotEmpty()) {
            transitions.removeAt(0)
            if (transitions.isNotEmpty()) {
                transitions.first().start()
            }
        }
        setChanged()
        notifyObservers(State.ON_TRANSITION_COMPLETED)
    }

    fun prepareOnDraw() {
        if (transitions.isNotEmpty()) {
            transitions.first().prepareOnDraw()
        }
    }

    fun onPause() {
        cancelCurrentTransition()
        replaceBitmapWithColor()
    }

    fun onAttached() {
        if (transitions.isNotEmpty()) {
            transitions.first().onAttached()
        }
    }

    fun onDetached() {
        cancelCurrentTransition()
        replaceBitmapWithColor()
    }

    fun drawableReplaced() {
        if (transitions.isNotEmpty()) {
            transitions.first().drawableReplaced()
        }
    }

    private inline fun cancelCurrentTransition() {
        if (transitions.isNotEmpty()) {
            transitions.first().onDetached()
        }
        transitions.clear()
    }

    private fun replaceBitmapWithColor() {
        if (humbleImageView.imageViewDrawables[Transition.CURRENT_IDX].getDrawable() is HumbleBitmapDrawable) {
            addAndStartTransition(PaletteTransition(humbleImageView.imageViewDrawables, this, androidPalette))
        }
    }

    private fun addAndStartTransition(transition: Transition) {
        if (transitions.isNotEmpty()) {
            transitions.add(1, transition)
        } else {
            transitions.add(transition)
            transitions.first().start()
        }
    }
}