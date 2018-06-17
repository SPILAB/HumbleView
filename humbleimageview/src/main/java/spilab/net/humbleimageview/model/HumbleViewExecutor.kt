package spilab.net.humbleimageview.model

import android.content.Context
import android.net.http.HttpResponseCache
import android.util.Log
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newFixedThreadPool
import kotlin.math.max

internal object HumbleViewExecutor {

    val executorService: ExecutorService by lazy {
        val threadCount = max(2, Runtime.getRuntime().availableProcessors() - 1)
        return@lazy newFixedThreadPool(threadCount)
    }
}