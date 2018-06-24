package spilab.net.humbleimageview.model.resourcetstates

import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.cache.OfflineCache


internal class RequestStateSearchInOfflineCache(stateContext: ResourceStateContext) :
        RequestState(stateContext) {

    private var offlineCache: OfflineCache = HumbleViewAPI.cache.getOfflineCache(stateContext.context.applicationContext)

    init {
        val bitmapData = offlineCache.get(stateContext.humbleResourceId.url)
        if (bitmapData != null) {
            stateContext.requestState = RequestStateDecode(stateContext, bitmapData)
        } else {
            stateContext.requestState = RequestStateDownload(stateContext)
        }
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
