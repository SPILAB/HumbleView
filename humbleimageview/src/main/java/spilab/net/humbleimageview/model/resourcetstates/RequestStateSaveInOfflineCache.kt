package spilab.net.humbleimageview.model.resourcetstates

import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.cache.OfflineCache


internal class RequestStateSaveInOfflineCache(stateContext: ResourceStateContext,
                                              bitmapData: ByteArray) :
        RequestState(stateContext) {

    private var offlineCache: OfflineCache = HumbleViewAPI.cache.getOfflineCache(stateContext.context.applicationContext)

    init {
        offlineCache.put(stateContext.humbleResourceId.url, bitmapData)
        stateContext.requestState = RequestStateSearchInOfflineCache(stateContext)
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
