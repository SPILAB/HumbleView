package spilab.net.humbleimageview.features.request

import android.content.Context
import android.content.res.Resources
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.drawable.DrawableDecoderTask
import spilab.net.humbleimageview.features.request.resourcetstates.ResourceStateContext

internal class HumbleResourceRequest(context: Context,
                                     val humbleResourceId: ResourceId,
                                     val offlineCache: Boolean,
                                     uiThreadHandler: AndroidHandler,
                                     resources: Resources,
                                     drawableDecoderTaskListener: DrawableDecoderTask.DrawableDecoderTaskListener) {

    private val resourceStateContext: ResourceStateContext = ResourceStateContext(context, resources, humbleResourceId,
            offlineCache, uiThreadHandler, drawableDecoderTaskListener)

    fun cancel() {
        resourceStateContext.requestState.cancel()
    }
}