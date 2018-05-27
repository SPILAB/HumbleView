package spilab.net.humbleviewimage.model.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import spilab.net.humbleviewimage.android.AndroidBitmapFactory

internal class HumbleBitmapFactory(private val androidBitmapFactory: AndroidBitmapFactory = AndroidBitmapFactory()) {

    var lastSampleSize: Int = 0

    fun decodeBitmapForSize(bitmapData: ByteArray, width: Int, height: Int): Bitmap? {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        androidBitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size, options)
        options.inJustDecodeBounds = false
        lastSampleSize = computeSampleSize(width, height, options)
        options.inSampleSize = lastSampleSize
        val recycleBitmap = BitmapPool.find(options.outWidth / options.inSampleSize,
                options.outHeight / options.inSampleSize)
        options.inBitmap = recycleBitmap
        return androidBitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size, options)
    }

    private inline fun computeSampleSize(width: Int, height: Int, options: BitmapFactory.Options): Int {
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