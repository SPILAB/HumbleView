package spilab.net.humbleimageview.model.resourcetstates

internal abstract class RequestState(val stateContext: ResourceStateContext) {

    abstract fun cancel()
}