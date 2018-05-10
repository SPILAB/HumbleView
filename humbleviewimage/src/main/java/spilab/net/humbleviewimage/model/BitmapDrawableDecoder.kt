package spilab.net.humbleviewimage.model

import android.content.res.Resources
import android.graphics.BitmapFactory
import spilab.net.humbleviewimage.android.AndroidBitmapFactory
import java.io.InputStream

internal class BitmapDrawableDecoder(private val bitmapFactory: AndroidBitmapFactory = AndroidBitmapFactory()) {

    fun decodeBitmapDrawableForViewSize(inputStream: InputStream,
                                        resources: Resources,
                                        id: HumbleBitmapId): HumbleBitmapDrawable? {
        val bitmapData = inputStream.readBytes()
        val options = bitmapFactory.decodeBitmapSize(bitmapData)
        val bitmap = bitmapFactory.decodeBitmap(bitmapData,
                computeBitmapScale(id.size.width, id.size.height, options))
        if (bitmap != null) {
            return HumbleBitmapDrawable(bitmap, id, resources, options.inSampleSize)
        }
        return null
    }

    private fun computeBitmapScale(width: Int, height: Int, options: BitmapFactory.Options): Int {
        val bitmapWidth = options.outWidth
        val bitmapHeight = options.outHeight
        var inSampleSize = 1
        if (bitmapWidth > width && bitmapHeight > height) {
            val halfBitmapWidth = bitmapWidth / 2
            val halfBitmapHeight = bitmapHeight / 2
            while (halfBitmapWidth / inSampleSize >= width
                    && halfBitmapHeight / inSampleSize >= height) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}