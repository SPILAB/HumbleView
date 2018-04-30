package spilab.net.humbleviewimage.model

import java.util.concurrent.Executors.newFixedThreadPool
import java.util.concurrent.ExecutorService

object HumbleViewModel {

    val executorService: ExecutorService by lazy {
        return@lazy newFixedThreadPool(2)
    }
}