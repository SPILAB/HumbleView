package spilab.net.humbleimageview.api

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newFixedThreadPool
import kotlin.math.max

open class ExecutorProvider {

    private var executorService: ExecutorService? = null

    open fun getExecutorService(): ExecutorService {
        if (executorService == null) {
            val threadCount = max(2, Runtime.getRuntime().availableProcessors() - 1)
            executorService = newFixedThreadPool(threadCount)
        }
        return executorService!!
    }

    fun reset() {
        executorService?.shutdown()
        executorService = null
    }
}