package spilab.net.humbleimageview.features.memory

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build


class BitmapRecycle(private val versionSdk: Int = Build.VERSION.SDK_INT) {

    @SuppressLint("NewApi")
    fun canRecycleWithInBitmap(candidate: Bitmap, width: Int, height: Int, inSampleSize: Int): Boolean {

        if (versionSdk >= Build.VERSION_CODES.KITKAT) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            val w = width / inSampleSize
            val h = height / inSampleSize
            val byteCount = w * h * getBytesPerPixel(candidate.config)
            return byteCount <= candidate.allocationByteCount
        }

        // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
        return (candidate.width == width && candidate.height == height && inSampleSize == 1)
    }

    @SuppressLint("NewApi")
    fun getSize(candidate: Bitmap): Int {
        return if (versionSdk >= Build.VERSION_CODES.KITKAT) {
            candidate.allocationByteCount
        } else {
            candidate.byteCount
        }
    }

    private fun getBytesPerPixel(config: Bitmap.Config): Int {
        return when (config) {
            Bitmap.Config.ARGB_8888 -> 4
            Bitmap.Config.RGB_565, Bitmap.Config.ARGB_4444 -> 2
            Bitmap.Config.ALPHA_8 -> 1
            else -> 1
        }
    }
}