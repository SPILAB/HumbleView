package spilab.net.humbleviewimage.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import spilab.net.humbleviewimage.HumbleViewImage
import java.net.HttpURLConnection
import java.net.URL

class HumbleViewDownloader(val view: HumbleViewImage) {

    internal fun start(url: String) {
        HumbleViewModel.executorService.submit({
            var bitmap: Bitmap? = null
            val uri = URL(url)
            try {
                val urlConnection = uri.openConnection() as HttpURLConnection
                try {
                    val statusCode = urlConnection.responseCode
                    if (statusCode == 200) {
                        val inputStream = urlConnection.inputStream
                        if (inputStream != null) {
                            bitmap = BitmapFactory.decodeStream(inputStream)
                        }
                    }
                } finally {
                    urlConnection.disconnect()
                }
            } catch (exception: Exception) {
                // For network exception, we do not need to log,
                // we simply cannot retrieve the resource from network.
            }
            if (bitmap != null) {
                view.post({
                    view.transitionTo(url, bitmap)
                })
            }
        })
    }
}