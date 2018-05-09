package spilab.net.humbleviewimage.model

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleviewimage.HumbleViewImage
import spilab.net.humbleviewimage.drawable.BitmapDrawableDecoder
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Future


internal class HumbleViewDownloader(val humbleViewImage: HumbleViewImage) {

    private var task: Future<*>? = null
    private var bitmapId: HumbleBitmapId? = null
    private val bitmapDecoder = BitmapDrawableDecoder()

    internal fun start(context: Context, resources: Resources, bitmapId: HumbleBitmapId) {
        val mainHandler = Handler(context.mainLooper)
        if (this.bitmapId != bitmapId) {
            task?.cancel(false)
        }
        this.bitmapId = bitmapId
        task = HumbleViewModel.executorService.submit({
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
                        humbleViewImage.onBitmapReady(bitmapId, drawable)
                    })
                }
            } finally {
                inputStream?.close()
                urlConnection?.disconnect()
            }
        })
    }
}