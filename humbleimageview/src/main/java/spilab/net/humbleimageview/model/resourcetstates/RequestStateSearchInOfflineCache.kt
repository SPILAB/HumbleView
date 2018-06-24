package spilab.net.humbleimageview.model.resourcetstates

import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.cache.OfflineCache
import spilab.net.humbleimageview.model.cache.OfflineCacheRead
import java.util.concurrent.Future


internal class RequestStateSearchInOfflineCache(stateContext: ResourceStateContext) :
        RequestState(stateContext), OfflineCacheRead.OfflineCacheReadListener {

    private var offlineCache: OfflineCache = HumbleViewAPI.cache.getOfflineCache(stateContext.context.applicationContext)
    private var task: Future<*>

    init {
        task = offlineCache.get(stateContext.humbleResourceId.url, this)
    }

    override fun onFileRead(data: ByteArray) {
        stateContext.requestState = RequestStateDecode(stateContext, data)
    }

    override fun onFileNotFound() {
        stateContext.requestState = RequestStateDownload(stateContext)
    }

    override fun cancel() {
        task.cancel(false)
    }
}
