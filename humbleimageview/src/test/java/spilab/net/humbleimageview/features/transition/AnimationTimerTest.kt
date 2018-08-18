package spilab.net.humbleimageview.features.transition

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AnimationTimerTest {

    companion object {
        const val DURATION_MILLIS = 1000L
        const val EPSILON = 0.1f
    }

    private lateinit var animationTimer: AnimationTimer
    private var uptimeMillis: Long = 0
    private val uptimeMillisProvider: () -> Long = { uptimeMillis }


    @Before
    fun beforeTests() {
        animationTimer = AnimationTimer(DURATION_MILLIS, uptimeMillisProvider)
    }

    @Test
    fun `Given an animation timer at begin, When normalize at 255, Then should return 0`() {
        uptimeMillis = 0
        animationTimer.startIfNeeded()
        Assert.assertEquals(0.0f, animationTimer.getNormalized(255.0f), EPSILON)
    }

    @Test
    fun `Given an animation timer at middle, When normalize at 255, Then should return 127,5`() {
        uptimeMillis = 0
        animationTimer.startIfNeeded()
        uptimeMillis = DURATION_MILLIS / 2
        Assert.assertEquals(127.5f, animationTimer.getNormalized(255.0f), EPSILON)
    }

    @Test
    fun `Given an animation timer at end, When normalize at 255, Then should return 255`() {
        uptimeMillis = 0
        animationTimer.startIfNeeded()
        uptimeMillis = DURATION_MILLIS
        Assert.assertEquals(255.0f, animationTimer.getNormalized(255.0f), EPSILON)
    }

    @Test
    fun `Given an animation timer after the end, When normalize at 255, Then should return 255`() {
        uptimeMillis = 0
        animationTimer.startIfNeeded()
        uptimeMillis = DURATION_MILLIS * 2
        Assert.assertEquals(255.0f, animationTimer.getNormalized(255.0f), EPSILON)
    }
}