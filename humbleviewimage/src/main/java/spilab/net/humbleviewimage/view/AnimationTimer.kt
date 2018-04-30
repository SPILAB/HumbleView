package spilab.net.humbleviewimage.view

import android.os.SystemClock
import kotlin.math.min


internal class AnimationTimer(val durationMillis: Long) {

    private var timerStartMillis = SystemClock.uptimeMillis()

    inline fun start() {
        timerStartMillis = SystemClock.uptimeMillis()
    }

    inline fun getNormalized(normalize: Float = 1.0f) = min(normalize,
            ((SystemClock.uptimeMillis() - timerStartMillis).toFloat() * normalize) / durationMillis.toFloat())
}