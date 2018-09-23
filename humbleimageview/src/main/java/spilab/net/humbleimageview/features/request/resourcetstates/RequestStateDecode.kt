package spilab.net.humbleimageview.features.request.resourcetstates

import spilab.net.humbleimageview.drawable.DrawableDecoderTask
import java.util.concurrent.Future

internal class RequestStateDecode(stateContext: ResourceStateContext, bitmapData: ByteArray) : RequestState(stateContext) {

    private var drawableDecoderTask: DrawableDecoderTask = DrawableDecoderTask(
            bitmapData,
            stateContext.resources,
            stateContext.humbleResourceId,
            stateContext.drawableDecoderTaskListener,
            stateContext.uiThreadHandler)
    private var task: Future<*>

    init {
        task = drawableDecoderTask.submit()
    }

    override fun cancel() {
        task.cancel(true)
    }
}