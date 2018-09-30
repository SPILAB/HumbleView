package spilab.net.humbleimageview.features.transform

import android.graphics.Bitmap

class DefaultTransformation : BitmapTransformation {

    override fun setValues(string: String) {}

    override fun transform(source: Bitmap): Bitmap {
        return source
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}