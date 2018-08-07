package spilab.net.humbleimageview.features.transition

import kotlin.math.min


internal class AnimationTimer(private val durationMillis: Long,
                              private val uptimeMillis: () -> Long) {

    companion object {
        private const val NOT_STARTED = -1L
    }

    private var timerStartMillis = NOT_STARTED

    fun startIfNeeded() {
        if (timerStartMillis == NOT_STARTED) {
            timerStartMillis = uptimeMillis()
        }
    }

    fun getNormalized(normalize: Float = 1.0f) = min(normalize,
            ((uptimeMillis() - timerStartMillis).toFloat() * normalize) / durationMillis.toFloat())
}