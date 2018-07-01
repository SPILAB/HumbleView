package spilab.net.humbleimageview.api

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newFixedThreadPool
import kotlin.math.max

class Executor {

    val executorService: ExecutorService by lazy {
        val threadCount = max(2, Runtime.getRuntime().availableProcessors() - 1)
        return@lazy newFixedThreadPool(threadCount)
    }
}