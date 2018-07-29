package spilab.net.humbleimageview.features.memory

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build


class BitmapRecycle(val versionSdk: Int = Build.VERSION.SDK_INT) {

    @SuppressLint("NewApi")
    fun canRecycleWithInBitmap(candidate: Bitmap, width: Int, height: Int, inSampleSize: Int): Boolean {

        if (versionSdk >= Build.VERSION_CODES.KITKAT) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            val byteCount = width * height * getBytesPerPixel(candidate.config)
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
        if (config === Bitmap.Config.ARGB_8888) {
            return 4
        } else if (config === Bitmap.Config.RGB_565) {
            return 2
        } else if (config === Bitmap.Config.ARGB_4444) {
            return 2
        } else if (config === Bitmap.Config.ALPHA_8) {
            return 1
        }
        return 1
    }
}