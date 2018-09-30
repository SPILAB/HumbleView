package spilab.net.humbleimageview.drawable

import android.content.res.Resources
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.features.decode.BitmapDecodeWithScale
import java.util.concurrent.Future

internal class DrawableDecoderTask(private val bitmapData: ByteArray,
                                   private val resources: Resources,
                                   private val resourceId: ResourceId,
                                   private val drawableDecoderTaskListener: DrawableDecoderTaskListener,
                                   private val uiThreadHandler: AndroidHandler,
                                   private val bitmapDecode: BitmapDecodeWithScale = BitmapDecodeWithScale()) {

    interface DrawableDecoderTaskListener {
        fun onDrawableDecoded(humbleBitmapDrawable: HumbleBitmapDrawable)
    }

    internal fun submit(): Future<*> {
        return HumbleViewAPI.executorProvider.getExecutorService().submit {
            var bitmap = bitmapDecode.decodeBitmapForSize(bitmapData,
                    resourceId.viewSize.width, resourceId.viewSize.height)
            if (bitmap != null) {
                bitmap = resourceId.bitmapTransform.transform(bitmap)
                val humbleBitmapDrawableRequest = HumbleBitmapDrawable(bitmap,
                        resourceId,
                        resources,
                        bitmapDecode.inSampleSize)
                uiThreadHandler.post(Runnable {
                    drawableDecoderTaskListener.onDrawableDecoded(humbleBitmapDrawableRequest)
                })
            }
        }
    }
}