package spilab.net.humbleviewimage.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory

internal class AndroidBitmapFactory {

    fun decodeByteArray(data: ByteArray, offset: Int, length: Int, opts: BitmapFactory.Options?): Bitmap? {
        return BitmapFactory.decodeByteArray(data, offset, length, opts)
    }
}
