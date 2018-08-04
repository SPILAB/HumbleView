package spilab.net.humbleimageview.features.transition

import kotlin.math.min


internal class AnimationTimer(private val durationMillis: Long,
                              private val uptimeMillis: () -> Long) {

    private var timerStartMillis = uptimeMillis()

    fun start() {
        timerStartMillis = uptimeMillis()
    }

    fun getNormalized(normalize: Float = 1.0f) = min(normalize,
            ((uptimeMillis() - timerStartMillis).toFloat() * normalize) / durationMillis.toFloat())
}