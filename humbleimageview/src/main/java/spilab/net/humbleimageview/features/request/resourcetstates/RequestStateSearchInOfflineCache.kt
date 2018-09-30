package spilab.net.humbleimageview.features.request.resourcetstates

import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.features.offlinecache.OfflineCacheInterface
import java.util.concurrent.Future


internal class RequestStateSearchInOfflineCache(stateContext: ResourceStateContext) :
        RequestState(stateContext), OfflineCacheInterface.OfflineCacheReadListener {

    private var offlineCacheInterface: OfflineCacheInterface = HumbleViewAPI.offlineCache.getOfflineCache(stateContext.context.applicationContext)
    private var task: Future<*>

    init {
        task = offlineCacheInterface.get(stateContext.resourceId.urlWithSize.url, this)
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
