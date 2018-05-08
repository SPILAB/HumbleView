package spilab.net.humbleviewimage.model

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newFixedThreadPool
import kotlin.math.max

internal object HumbleViewModel {

    val executorService: ExecutorService by lazy {
        val threadCount = max(2, Runtime.getRuntime().availableProcessors() - 1)
        return@lazy newFixedThreadPool(threadCount)
    }
}