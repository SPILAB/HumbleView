package spilab.net.humbleimageview.model.drawable

import android.content.res.Resources
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.features.decode.BitmapDecodeWithScale
import java.util.concurrent.Future

internal class DrawableDecoderTask(private val bitmapData: ByteArray,
                                   private val resources: Resources,
                                   private val humbleResourceId: HumbleResourceId,
                                   private val drawableDecoderTaskListener: DrawableDecoderTaskListener,
                                   private val uiThreadHandler: AndroidHandler,
                                   private val bitmapDecode: BitmapDecodeWithScale = BitmapDecodeWithScale()) {

    interface DrawableDecoderTaskListener {
        fun onDrawableDecoded(humbleBitmapDrawable: HumbleBitmapDrawable)
    }

    internal fun submit(): Future<*> {
        return HumbleViewAPI.executorProvider.getExecutorService().submit {
            val bitmap = bitmapDecode.decodeBitmapForSize(bitmapData,
                    humbleResourceId.viewSize.width, humbleResourceId.viewSize.height)
            if (bitmap != null) {
                val humbleBitmapDrawableRequest = HumbleBitmapDrawable(bitmap,
                        humbleResourceId,
                        resources,
                        bitmapDecode.inSampleSize)
                uiThreadHandler.post(Runnable {
                    drawableDecoderTaskListener.onDrawableDecoded(humbleBitmapDrawableRequest)
                })
            }
        }
    }
}