package spilab.net.humbleviewimage.view

import android.graphics.BitmapFactory
import spilab.net.humbleviewimage.android.AndroidBitmapFactory
import spilab.net.humbleviewimage.model.BitmapDebug
import spilab.net.humbleviewimage.model.HumbleViewBitmap
import java.io.InputStream

internal class HumbleViewSize(private val width: Int,
                              private val height: Int,
                              private val bitmapFactory: AndroidBitmapFactory = AndroidBitmapFactory()) {

    fun decodeBitmapForViewSize(inputStream: InputStream): HumbleViewBitmap? {
        val bitmapData = inputStream.readBytes()
        val options = bitmapFactory.decodeBitmapSize(bitmapData)
        var bitmap = bitmapFactory.decodeBitmap(bitmapData,
                computeBitmapScale(options))
        if (bitmap != null) {
            return HumbleViewBitmap(bitmap, BitmapDebug(options.inSampleSize))
        }
        return null
    }

    private fun computeBitmapScale(options: BitmapFactory.Options): Int {
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