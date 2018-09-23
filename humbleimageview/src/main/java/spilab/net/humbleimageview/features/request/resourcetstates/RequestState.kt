package spilab.net.humbleimageview.features.request.resourcetstates

internal abstract class RequestState(val stateContext: ResourceStateContext) {

    abstract fun cancel()
}