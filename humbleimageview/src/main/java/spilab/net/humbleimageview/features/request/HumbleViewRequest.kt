package spilab.net.humbleimageview.features.request

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.drawable.DrawableDecoderTask
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.sizelist.UrlsWithSizes
import spilab.net.humbleimageview.features.transform.DefaultTransformation
import spilab.net.humbleimageview.features.transform.BitmapTransformation
import spilab.net.humbleimageview.view.ViewSize

internal class HumbleViewRequest(private val context: Context,
                                 private var resources: Resources,
                                 private val uiThreadHandler: Handler) : DrawableDecoderTask.DrawableDecoderTaskListener {

    private var currentId: ResourceId? = null
    private var humbleResourceRequest: HumbleResourceRequest? = null

    var offlineCache: Boolean = false

    var urls: UrlsWithSizes? = null
        set(value) {
            field = value
            requestImageIfNeeded()
        }

    var viewSize: ViewSize? = null
        set(value) {
            field = value
            requestImageIfNeeded()
        }

    var bitmapTransform: BitmapTransformation = DefaultTransformation()
        set(value) {
            field = value
            requestImageIfNeeded()
        }

    var drawableEventsListener: DrawableEventsListener? = null

    fun requestImageIfNeeded() {
        if (urls != null && viewSize != null) {
            currentId = ResourceId(urls!!.getBestUrlWithSize(viewSize!!), viewSize!!, bitmapTransform)
            if (drawableEventsListener?.isCurrentOrNextDrawableIdEqualTo(currentId!!) == false) {
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
        if (currentId == humbleBitmapDrawable.resourceId) {
            drawableEventsListener?.onDrawableReady(humbleBitmapDrawable)
        }
    }
}