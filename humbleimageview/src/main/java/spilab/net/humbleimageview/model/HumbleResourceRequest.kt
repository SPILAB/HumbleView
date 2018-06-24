package spilab.net.humbleimageview.model

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.model.drawable.DrawableDecoderTask
import spilab.net.humbleimageview.model.resourcetstates.ResourceStateContext

internal class HumbleResourceRequest(context: Context,
                                     val humbleResourceId: HumbleResourceId,
                                     val offlineCache: Boolean,
                                     uiThreadHandler: Handler,
                                     resources: Resources,
                                     drawableDecoderTaskListener: DrawableDecoderTask.DrawableDecoderTaskListener) {

    private val resourceStateContext: ResourceStateContext

    init {
        resourceStateContext = ResourceStateContext(context, resources, humbleResourceId,
                offlineCache, uiThreadHandler, drawableDecoderTaskListener)
    }

    fun cancel() {
        resourceStateContext.requestState.cancel()
    }
}