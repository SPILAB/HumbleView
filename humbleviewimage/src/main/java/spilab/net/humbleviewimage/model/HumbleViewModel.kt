package spilab.net.humbleviewimage.model

import spilab.net.humbleviewimage.presenter.HumbleViewPresenter
import spilab.net.humbleviewimage.view.HumbleTransition

internal class HumbleViewModel(private val presenter: HumbleViewPresenter,
                               lastKnownSize: ViewSize? = null,
                               var debug: Boolean = false) {

    var url: String? = null
        set(value) {
            field = value
            updateImageIfNeeded()
        }

    var viewSize = lastKnownSize
        set(value) {
            field = value
            updateImageIfNeeded()
        }

    private val downloaderLazy = lazy { HumbleViewDownloader(this) }
    private val downloader: HumbleViewDownloader by downloaderLazy
    private var currentBitmapId: HumbleBitmapId? = null

    fun updateImageIfNeeded() {
        if (url != null && viewSize != null) {
            currentBitmapId = HumbleBitmapId(url!!, viewSize!!)
            if (!presenter.isCurrentOrNextDrawableId(currentBitmapId!!) /*&& currentBitmapId != nextBitmapId*/) {
                downloader.start(presenter.getApplicationContext(),
                        presenter.getResources(), currentBitmapId!!)
            }
        }
    }

    fun onBitmapReady(drawable: HumbleBitmapDrawable) {
        if (drawable != null && currentBitmapId != null && currentBitmapId == drawable.humbleBitmapId) {
            presenter.setTransition(HumbleTransition(drawable))
        }
    }

    fun cancelDownloadIfNeeded() {
        if (downloaderLazy.isInitialized()) {
            downloader.cancel()
        }
    }
}