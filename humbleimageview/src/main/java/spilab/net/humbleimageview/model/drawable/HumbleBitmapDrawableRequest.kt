package spilab.net.humbleimageview.model.drawable

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import spilab.net.humbleimageview.log.HumbleLogs
import spilab.net.humbleimageview.model.HumbleBitmapId
import spilab.net.humbleimageview.api.HumbleViewAPI
import java.io.IOException

internal class HumbleBitmapDrawableRequest(private val context: Context,
                                           val humbleBitmapId: HumbleBitmapId,
                                           private val uiThreadHandler: Handler,
                                           private var resources: Resources,
                                           private val drawableDecoderTaskListener: DrawableDecoderTask.DrawableDecoderTaskListener)
    : Callback {

    private var drawableDecoderTask: DrawableDecoderTask? = null
    private val requestCall: Call

    init {
        val request = Request.Builder().url(humbleBitmapId.url).build()
        requestCall = HumbleViewAPI.http.getOkHttpClient(context).newCall(request)
        requestCall.enqueue(this)
    }

    fun cancel() {
        requestCall.cancel()
    }

    override fun onResponse(call: Call?, response: Response?) {
        if (response?.body() != null) {
            val bitmapData = response.body()?.bytes()
            if (bitmapData != null) {
                drawableDecoderTask = DrawableDecoderTask(
                        bitmapData,
                        resources,
                        humbleBitmapId,
                        drawableDecoderTaskListener,
                        uiThreadHandler)
                drawableDecoderTask?.submit()
            }
        }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        HumbleLogs.log("Cannot download bitmap from %s.", humbleBitmapId.url)
    }
}