package spilab.net.humbleimageview.model.drawable

import android.content.res.Resources
import spilab.net.humbleimageview.model.HumbleBitmapId
import spilab.net.humbleimageview.model.bitmap.HumbleBitmapFactory
import java.io.InputStream

internal class DrawableDecoder(private val humbleBitmapFactory: HumbleBitmapFactory = HumbleBitmapFactory(),
                               private val resources: Resources) {

    fun decodeBitmapDrawableForViewSize(inputStream: InputStream,
                                        id: HumbleBitmapId): HumbleBitmapDrawable? {
        val bitmapData = inputStream.readBytes()
        val bitmap = humbleBitmapFactory.decodeBitmapForSize(bitmapData, id.size.width, id.size.height)
        if (bitmap != null) {
            return HumbleBitmapDrawable(bitmap, id, resources, humbleBitmapFactory.lastSampleSize)
        }
        return null
    }
}