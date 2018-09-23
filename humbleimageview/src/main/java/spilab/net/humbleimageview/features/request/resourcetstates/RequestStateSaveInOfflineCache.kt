package spilab.net.humbleimageview.features.request.resourcetstates

import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.features.offlinecache.OfflineCacheInterface
import java.util.concurrent.Future


internal class RequestStateSaveInOfflineCache(stateContext: ResourceStateContext,
                                              bitmapData: ByteArray) :
        RequestState(stateContext), OfflineCacheInterface.OfflineCacheWriteListener {

    private var offlineCache: OfflineCacheInterface = HumbleViewAPI.offlineCache.getOfflineCache(stateContext.context.applicationContext)
    private var task: Future<*>

    init {
        task = offlineCache.put(stateContext.humbleResourceId.urlWithSize.url, bitmapData, this)
    }

    override fun onFileWriteComplete() {
        stateContext.requestState = RequestStateSearchInOfflineCache(stateContext)
    }

    override fun cancel() {
        task.cancel(false)
    }
}
