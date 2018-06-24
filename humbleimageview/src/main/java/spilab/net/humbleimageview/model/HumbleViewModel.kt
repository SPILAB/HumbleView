package spilab.net.humbleimageview.model

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.model.drawable.DrawableDecoderTask
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.presenter.HumbleViewPresenter

internal class HumbleViewModel(private val context: Context,
                               private val presenter: HumbleViewPresenter,
                               private var resources: Resources,
                               private val uiThreadHandler: Handler) : DrawableDecoderTask.DrawableDecoderTaskListener {

    private var currentId: HumbleResourceId? = null
    private var humbleResourceRequest: HumbleResourceRequest? = null

    var offlineCache: Boolean = false

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
            currentId = HumbleResourceId(url!!, viewSize!!)
            if (!presenter.isCurrentOrNextDrawableId(currentId!!)) {
                if (humbleResourceRequest?.humbleResourceId != currentId) {
                    humbleResourceRequest?.cancel()
                    humbleResourceRequest = HumbleResourceRequest(
                            context,
                            currentId!!,
                            offlineCache,
                            AndroidHandler(uiThreadHandler),
                            resources,
                            this
                    )
                }
            }
        }
    }

    fun cancel() {
        humbleResourceRequest?.cancel()
        humbleResourceRequest = null
        currentId = null
    }

    override
    fun onDrawableDecoded(humbleBitmapDrawable: HumbleBitmapDrawable) {
        humbleResourceRequest = null
        if (currentId == humbleBitmapDrawable.humbleResourceId) {
            presenter.addTransitionDrawable(humbleBitmapDrawable)
        }
    }
}