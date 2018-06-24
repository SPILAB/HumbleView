package spilab.net.humbleimageview.model.drawable

import android.content.res.Resources
import android.os.Handler
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.HumbleViewExecutor
import spilab.net.humbleimageview.model.bitmap.HumbleBitmapFactory

internal class DrawableDecoderTask(private val bitmapData: ByteArray,
                                   private val resources: Resources,
                                   private val humbleResourceId: HumbleResourceId,
                                   private val drawableDecoderTaskListener: DrawableDecoderTaskListener,
                                   private val uiThreadHandler: Handler,
                                   private val humbleBitmapFactory: HumbleBitmapFactory = HumbleBitmapFactory()) {

    interface DrawableDecoderTaskListener {
        fun onDrawableDecoded(humbleBitmapDrawable: HumbleBitmapDrawable)
    }

    internal fun submit() {
        HumbleViewExecutor.executorService.submit {
            val bitmap = humbleBitmapFactory.decodeBitmapForSize(bitmapData,
                    humbleResourceId.viewSize.width, humbleResourceId.viewSize.height)
            if (bitmap != null) {
                val humbleBitmapDrawableRequest = HumbleBitmapDrawable(bitmap,
                        humbleResourceId,
                        resources,
                        humbleBitmapFactory.lastSampleSize)
                uiThreadHandler.post {
                    drawableDecoderTaskListener.onDrawableDecoded(humbleBitmapDrawableRequest)
                }
            }
        }
    }
}