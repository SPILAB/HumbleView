package spilab.net.humbleviewimage.model

import android.content.Context
import android.os.Handler
import spilab.net.humbleviewimage.android.AndroidHttpURLConnection
import spilab.net.humbleviewimage.model.cache.HumbleViewDownloader
import spilab.net.humbleviewimage.model.drawable.DrawableDecoder
import spilab.net.humbleviewimage.model.drawable.HumbleBitmapDrawable
import spilab.net.humbleviewimage.presenter.HumbleViewPresenter

internal class HumbleViewModel(private val presenter: HumbleViewPresenter,
                               private val bitmapDrawableDecoder: DrawableDecoder,
                               var debug: Boolean = false,
                               context: Context) {

    var url: String? = null
        set(value) {
            field = value
            updateImageIfNeeded()
        }

    var viewSize: ViewSize? = null
        set(value) {
            field = value
            updateImageIfNeeded()
        }

    private val downloaderLazy = lazy {
        HumbleViewDownloader(
                AndroidHttpURLConnection(),
                bitmapDrawableDecoder,
                Handler(context.mainLooper),
                this)
    }
    private val downloader: HumbleViewDownloader by downloaderLazy
    private var currentBitmapId: HumbleBitmapId? = null

    fun updateImageIfNeeded() {
        if (url != null && viewSize != null) {
            currentBitmapId = HumbleBitmapId(url!!, viewSize!!)
            if (!presenter.isCurrentOrNextDrawableId(currentBitmapId!!)) {
                downloader.start(currentBitmapId!!)
            }
        }
    }

    fun onBitmapReady(drawable: HumbleBitmapDrawable) {
        if (drawable != null && currentBitmapId != null && currentBitmapId == drawable.humbleBitmapId) {
            presenter.addTransitionDrawable(drawable)
        }
    }

    fun cancelDownloadIfNeeded() {
        if (downloaderLazy.isInitialized()) {
            downloader.cancel()
        }
    }
}