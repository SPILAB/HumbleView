package spilab.net.humbleimageview.model.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import spilab.net.humbleimageview.android.AndroidBitmapFactory
import spilab.net.humbleimageview.feature.memory.BitmapPool

internal class HumbleBitmapFactory(private val androidBitmapFactory: AndroidBitmapFactory = AndroidBitmapFactory()) {

    var inSampleSize: Int = 0

    fun decodeBitmapForSize(bitmapData: ByteArray, width: Int, height: Int): Bitmap? {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        androidBitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size, options)
        options.inJustDecodeBounds = false
        inSampleSize = computeSampleSize(width, height, options)
        options.inSampleSize = inSampleSize
        val recycleBitmap = BitmapPool.find(options.outWidth / options.inSampleSize,
                options.outHeight / options.inSampleSize, inSampleSize)
        options.inBitmap = recycleBitmap
        options.inMutable = true
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