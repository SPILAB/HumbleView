package spilab.net.humbleimageview.model.drawable

import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.model.HumbleBitmapId
import spilab.net.humbleimageview.model.HumbleViewExecutor
import spilab.net.humbleimageview.model.bitmap.HumbleBitmapFactory

internal class DrawableDecoderTask(private val bitmapData: ByteArray,
                                   private val resources: Resources,
                                   private val humbleBitmapId: HumbleBitmapId,
                                   private val drawableDecoderTaskListener: DrawableDecoderTaskListener,
                                   private val uiThreadHandler: Handler,
                                   private val humbleBitmapFactory: HumbleBitmapFactory = HumbleBitmapFactory()) {

    interface DrawableDecoderTaskListener {
        fun onDrawableDecoded(humbleBitmapDrawable: HumbleBitmapDrawable)
    }

    internal fun submit() {
        HumbleViewExecutor.executorService.submit {
            val bitmap = humbleBitmapFactory.decodeBitmapForSize(bitmapData,
                    humbleBitmapId.viewSize.width, humbleBitmapId.viewSize.height)
            if (bitmap != null) {
                val humbleBitmapDrawableRequest = HumbleBitmapDrawable(bitmap,
                        humbleBitmapId,
                        resources,
                        humbleBitmapFactory.lastSampleSize)
                uiThreadHandler.post {
                    drawableDecoderTaskListener.onDrawableDecoded(humbleBitmapDrawableRequest)
                }
            }
        }
    }
}