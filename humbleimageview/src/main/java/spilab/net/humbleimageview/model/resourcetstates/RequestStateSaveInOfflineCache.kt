package spilab.net.humbleimageview.model.resourcetstates

import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.cache.OfflineCache
import spilab.net.humbleimageview.model.cache.OfflineCacheWrite
import java.util.concurrent.Future


internal class RequestStateSaveInOfflineCache(stateContext: ResourceStateContext,
                                              bitmapData: ByteArray) :
        RequestState(stateContext), OfflineCacheWrite.OfflineCacheWriteListener {

    private var offlineCache: OfflineCache = HumbleViewAPI.cache.getOfflineCache(stateContext.context.applicationContext)
    private var task: Future<*>

    init {
        task = offlineCache.put(stateContext.humbleResourceId.url, bitmapData, this)
    }

    override fun onFileWriteComplete() {
        stateContext.requestState = RequestStateSearchInOfflineCache(stateContext)
    }

    override fun cancel() {
        task.cancel(false)
    }
}
