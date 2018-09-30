package spilab.net.humbleimageview.features.transform

import android.graphics.Bitmap

/**
 * TODO: implement equal / hash code
 */
interface BitmapTransformation {
    fun setValues(string: String)

    fun transform(source: Bitmap): Bitmap
}