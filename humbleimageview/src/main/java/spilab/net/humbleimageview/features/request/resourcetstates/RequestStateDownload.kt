package spilab.net.humbleimageview.features.request.resourcetstates

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.log.HumbleLogs
import java.io.IOException

internal class RequestStateDownload(stateContext: ResourceStateContext) :
        RequestState(stateContext), Callback {

    private val requestCall: Call

    init {
        val request = Request.Builder().url(stateContext.resourceId.urlWithSize.url).build()
        requestCall = HumbleViewAPI.http.getOkHttpClient(stateContext.context).newCall(request)
        requestCall.enqueue(this)
    }

    override fun cancel() {
        requestCall.cancel()
    }

    override fun onResponse(call: Call?, response: Response?) {
        if (response?.body() != null) {
            val bitmapData = response.body()?.bytes()
            if (bitmapData != null) {
                if (stateContext.offlineCache) {
                    stateContext.requestState = RequestStateSaveInOfflineCache(stateContext, bitmapData)
                } else {
                    stateContext.requestState = RequestStateDecode(stateContext, bitmapData)
                }
            }
        }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        HumbleLogs.log("Cannot download bitmap from %s.", stateContext.resourceId.urlWithSize)
    }
}