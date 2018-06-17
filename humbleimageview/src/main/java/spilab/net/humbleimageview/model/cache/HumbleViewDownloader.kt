package spilab.net.humbleimageview.model.cache

import android.os.Handler
import spilab.net.humbleimageview.android.AndroidHttpURLConnection
import spilab.net.humbleimageview.model.HumbleBitmapId
import spilab.net.humbleimageview.model.HumbleViewExecutor
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.concurrent.Future


internal class HumbleViewDownloader(private val uiThreadHandler: Handler,
                                    private val httpURLConnection: AndroidHttpURLConnection,
                                    private val humbleViewDownloaderListener: HumbleViewDownloaderListener) {

    @get:Synchronized
    @set:Synchronized
    private var bitmapId: HumbleBitmapId? = null

    private var task: Future<*>? = null

    interface HumbleViewDownloaderListener {
        fun onDownloadComplete(bitmapData: ByteArray)
    }

    internal fun start(humbleBitmapId: HumbleBitmapId) {
        if (bitmapId != humbleBitmapId) {
            cancel()
        }
        if (bitmapId == humbleBitmapId) {
            return
        }

        bitmapId = humbleBitmapId
        task = HumbleViewExecutor.executorService.submit {
            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            try {
                urlConnection = httpURLConnection.openConnection(humbleBitmapId.url)
                val statusCode = urlConnection.responseCode
                if (statusCode == 200) {
                    inputStream = urlConnection.inputStream
                    if (inputStream != null) {
                        val bitmapData = inputStream.readBytes()
                        uiThreadHandler.post {
                            humbleViewDownloaderListener.onDownloadComplete(bitmapData)
                        }
                    }
                }
            } catch (t: Throwable) {
                resetCurrentBitmapId()
            } finally {
                urlConnection?.disconnect()
                inputStream?.close()
            }
        }
    }

    internal fun cancel() {
        resetCurrentBitmapId()
        task?.cancel(true)
        task = null
    }

    private inline fun resetCurrentBitmapId() {
        bitmapId = null
    }
}