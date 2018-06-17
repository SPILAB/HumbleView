package spilab.net.humbleimageview.model.drawable

import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.android.AndroidHttpURLConnection
import spilab.net.humbleimageview.model.HumbleBitmapId
import spilab.net.humbleimageview.model.cache.HumbleViewDownloader

internal class HumbleBitmapDrawableRequest(val humbleBitmapId: HumbleBitmapId,
                                           private val uiThreadHandler: Handler,
                                           private var resources: Resources,
                                           httpURLConnection: AndroidHttpURLConnection,
                                           private val drawableDecoderTaskListener: DrawableDecoderTask.DrawableDecoderTaskListener)
    : HumbleViewDownloader.HumbleViewDownloaderListener {

    private var humbleViewDownloader: HumbleViewDownloader? = null
    private var drawableDecoderTask: DrawableDecoderTask? = null

    init {
        humbleViewDownloader = HumbleViewDownloader(uiThreadHandler,
                httpURLConnection, this)
        humbleViewDownloader?.start(humbleBitmapId)
    }

    fun cancel() {
        humbleViewDownloader?.cancel()
    }

    override
    fun onDownloadComplete(bitmapData: ByteArray) {
        drawableDecoderTask = DrawableDecoderTask(bitmapData,
                resources,
                humbleBitmapId,
                drawableDecoderTaskListener,
                uiThreadHandler)
        drawableDecoderTask?.submit()
    }
}