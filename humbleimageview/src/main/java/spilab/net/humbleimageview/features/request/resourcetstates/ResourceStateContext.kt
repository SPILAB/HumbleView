package spilab.net.humbleimageview.features.request.resourcetstates

import android.content.Context
import android.content.res.Resources
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.drawable.DrawableDecoderTask

internal class ResourceStateContext(val context: Context,
                                    val resources: Resources,
                                    val humbleResourceId: ResourceId,
                                    val offlineCache: Boolean,
                                    val uiThreadHandler: AndroidHandler,
                                    val drawableDecoderTaskListener: DrawableDecoderTask.DrawableDecoderTaskListener) {

    var requestState: RequestState = if (offlineCache) {
        RequestStateSearchInOfflineCache(this)
    } else {
        RequestStateDownload(this)
    }

}