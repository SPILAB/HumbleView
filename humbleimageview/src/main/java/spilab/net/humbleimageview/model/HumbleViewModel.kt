package spilab.net.humbleimageview.model

import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.android.AndroidHttpURLConnection
import spilab.net.humbleimageview.model.drawable.DrawableDecoderTask
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawableRequest
import spilab.net.humbleimageview.presenter.HumbleViewPresenter

internal class HumbleViewModel(private val presenter: HumbleViewPresenter,
                               private var resources: Resources,
                               private val uiThreadHandler: Handler) : DrawableDecoderTask.DrawableDecoderTaskListener {

    private var currentId: HumbleBitmapId? = null
    private var humbleBitmapDrawableRequest: HumbleBitmapDrawableRequest? = null

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

    fun updateImageIfNeeded() {
        if (url != null && viewSize != null) {
            currentId = HumbleBitmapId(url!!, viewSize!!)
            if (!presenter.isCurrentOrNextDrawableId(currentId!!)) {
                if (humbleBitmapDrawableRequest?.humbleBitmapId != currentId) {
                    humbleBitmapDrawableRequest?.cancel()
                    humbleBitmapDrawableRequest = HumbleBitmapDrawableRequest(
                            currentId!!,
                            uiThreadHandler,
                            resources,
                            AndroidHttpURLConnection(),
                            this)
                }
            }
        }
    }

    fun cancel() {
        humbleBitmapDrawableRequest?.cancel()
        humbleBitmapDrawableRequest = null
        currentId = null
    }

    override
    fun onDrawableDecoded(humbleBitmapDrawable: HumbleBitmapDrawable) {
        humbleBitmapDrawableRequest = null
        if (currentId == humbleBitmapDrawable.humbleBitmapId) {
            presenter.addTransitionDrawable(humbleBitmapDrawable)
        }
    }
}