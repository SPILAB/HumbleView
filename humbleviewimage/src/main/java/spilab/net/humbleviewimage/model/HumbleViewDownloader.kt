package spilab.net.humbleviewimage.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import spilab.net.humbleviewimage.HumbleViewImage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Future


class HumbleViewDownloader(val view: HumbleViewImage) {

    private var task: Future<*>? = null
    private var url: String? = null

    internal fun start(url: String, context: Context) {
        val mainHandler = Handler(context.mainLooper)
        if (this.url != url) {
            val canceled = task?.cancel(false)
        }
        this.url = url
        task = HumbleViewModel.executorService.submit({
            var bitmap: Bitmap? = null
            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            val uri = URL(url)
            try {
                urlConnection = uri.openConnection() as HttpURLConnection
                val statusCode = urlConnection.responseCode
                if (statusCode == 200) {
                    inputStream = urlConnection.inputStream
                    if (inputStream != null) {
                        bitmap = BitmapFactory.decodeStream(inputStream)
                    }
                }
                if (bitmap != null) {
                    mainHandler.post({
                        view.transitionTo(url, bitmap)
                    })
                }
            } finally {
                inputStream?.close()
                urlConnection?.disconnect()
            }
        })
    }
}