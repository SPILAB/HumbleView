package spilab.net.humbleviewimage.model.cache

import android.os.Handler
import spilab.net.humbleviewimage.android.AndroidHttpURLConnection
import spilab.net.humbleviewimage.model.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.concurrent.Future


internal class HumbleViewDownloader(private val httpURLConnection: AndroidHttpURLConnection,
                                    private val model: HumbleViewModel,
                                    private val drawableDecoder: BitmapDrawableDecoder,
                                    private val handler: Handler) {

    @get:Synchronized
    @set:Synchronized
    private var bitmapId: HumbleBitmapId? = null

    private var task: Future<*>? = null

    internal fun start(humbleBitmapId: HumbleBitmapId) {
        if (bitmapId != humbleBitmapId) {
            cancel()
        }
        if (bitmapId == humbleBitmapId) {
            return
        }
        bitmapId = humbleBitmapId
        task = HumbleViewExecutor.executorService.submit({
            var drawable: HumbleBitmapDrawable? = null
            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            try {
                urlConnection = httpURLConnection.openConnection(humbleBitmapId.url)
                val statusCode = urlConnection.responseCode
                if (statusCode == 200) {
                    inputStream = urlConnection.inputStream
                    if (inputStream != null) {
                        drawable = drawableDecoder.decodeBitmapDrawableForViewSize(
                                inputStream,
                                humbleBitmapId)
                    }
                }
                if (drawable != null) {
                    handler.post({
                        model.onBitmapReady(drawable)
                        resetCurrentBitmapId()
                    })
                }
            } catch (t: Throwable) {
                resetCurrentBitmapId()
            } finally {
                inputStream?.close()
                urlConnection?.disconnect()
            }
        })
    }

    internal fun cancel() {
        task?.cancel(true)
        task = null
    }

    private inline fun resetCurrentBitmapId() {
        bitmapId = null
    }
}