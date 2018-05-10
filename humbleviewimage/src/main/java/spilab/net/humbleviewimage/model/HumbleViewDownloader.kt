package spilab.net.humbleviewimage.model

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Future


internal class HumbleViewDownloader(val humbleViewImage: HumbleViewModel) {

    private var task: Future<*>? = null
    private var bitmapId: HumbleBitmapId? = null
    private val bitmapDecoder = BitmapDrawableDecoder()

    internal fun start(context: Context, resources: Resources, bitmapId: HumbleBitmapId) {
        HumbleViewExecutor.installHTTPCache(context.applicationContext)
        val mainHandler = Handler(context.mainLooper)
        if (this.bitmapId != bitmapId) {
            cancel()
        }
        this.bitmapId = bitmapId
        task = HumbleViewExecutor.executorService.submit({
            var drawable: HumbleBitmapDrawable ? = null
            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            val uri = URL(bitmapId.url)
            try {
                urlConnection = uri.openConnection() as HttpURLConnection
                val statusCode = urlConnection.responseCode
                if (statusCode == 200) {
                    inputStream = urlConnection.inputStream
                    if (inputStream != null) {
                        drawable = bitmapDecoder.decodeBitmapDrawableForViewSize(inputStream, resources, bitmapId)
                    }
                }
                if (drawable != null) {
                    mainHandler.post({
                        humbleViewImage.onBitmapReady(drawable)
                    })
                }
            } finally {
                inputStream?.close()
                urlConnection?.disconnect()
            }
        })
    }

    internal fun cancel() {
        task?.cancel(true)
    }
}