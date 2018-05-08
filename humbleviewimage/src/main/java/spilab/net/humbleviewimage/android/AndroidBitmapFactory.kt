package spilab.net.humbleviewimage.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class AndroidBitmapFactory {

    val options = BitmapFactory.Options()

    fun decodeBitmapSize(bitmapData: ByteArray): BitmapFactory.Options {
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size, options)
        options.inJustDecodeBounds = false
        return options
    }

    fun decodeBitmap(bitmapData: ByteArray, sampleSize: Int): Bitmap? {
        options.inSampleSize = sampleSize
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size, options)
    }
}