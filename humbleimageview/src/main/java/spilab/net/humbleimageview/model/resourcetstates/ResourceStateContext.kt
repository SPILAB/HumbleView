package spilab.net.humbleimageview.model.resourcetstates

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.drawable.DrawableDecoderTask

internal class ResourceStateContext(val context: Context,
                                    val resources: Resources,
                                    val humbleResourceId: HumbleResourceId,
                                    val offlineCache: Boolean,
                                    val uiThreadHandler: Handler,
                                    val drawableDecoderTaskListener: DrawableDecoderTask.DrawableDecoderTaskListener) {

    var requestState: RequestState = if (offlineCache) {
        RequestStateSearchInOfflineCache(this)
    } else {
        RequestStateDownload(this)
    }

}