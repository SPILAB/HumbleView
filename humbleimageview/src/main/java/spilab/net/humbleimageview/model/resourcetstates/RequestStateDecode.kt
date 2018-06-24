package spilab.net.humbleimageview.model.resourcetstates

import spilab.net.humbleimageview.model.drawable.DrawableDecoderTask

internal class RequestStateDecode(stateContext: ResourceStateContext, bitmapData: ByteArray) : RequestState(stateContext) {

    private var drawableDecoderTask: DrawableDecoderTask = DrawableDecoderTask(
            bitmapData,
            stateContext.resources,
            stateContext.humbleResourceId,
            stateContext.drawableDecoderTaskListener,
            stateContext.uiThreadHandler)

    init {
        drawableDecoderTask.submit()
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}