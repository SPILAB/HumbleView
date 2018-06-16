package spilab.net.humbleimageview.model.cache

import android.os.Handler
import spilab.net.humbleimageview.android.AndroidHttpURLConnection
import spilab.net.humbleimageview.model.*
import spilab.net.humbleimageview.model.drawable.DrawableDecoder
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.util.concurrent.Future


internal class HumbleViewDownloader(private val httpURLConnection: AndroidHttpURLConnection,
                                    private val bitmapDrawableDecoder: DrawableDecoder,
                                    private val handler: Handler,
                                    model: HumbleViewModel) {

    private val model: WeakReference<HumbleViewModel> = WeakReference(model)

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
                        drawable = bitmapDrawableDecoder.decodeBitmapDrawableForViewSize(inputStream, humbleBitmapId)
                    }
                }
                if (drawable != null) {
                    handler.post({
                        model.get()?.onBitmapReady(drawable)
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
        resetCurrentBitmapId()
        task?.cancel(true)
        task = null
    }

    private inline fun resetCurrentBitmapId() {
        bitmapId = null
    }
}