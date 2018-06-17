package spilab.net.humbleimageview.model.bitmap

import android.graphics.Bitmap
import spilab.net.humbleimageview.log.HumbleLogs
import java.util.*

internal object BitmapPool {

    private val bitmaps = WeakHashMap<Bitmap, Boolean>()

    @Synchronized
    fun put(bitmap: Bitmap) {
        HumbleLogs.log("BitmapPool put=%s.", bitmap.toString())
        bitmaps[bitmap] = true
    }

    @Synchronized
    fun find(width: Int, height: Int): Bitmap? {
        HumbleLogs.log("BitmapPool searching in %s elements.", bitmaps.size.toString())
        var recycleBitmap: Bitmap? = null
        for (bitmap in bitmaps) {
            val bmp = bitmap.key
            if (bmp.width >= width && bmp.height >= height) {
                if (recycleBitmap == null || recycleBitmap.allocationByteCount > bmp.allocationByteCount) {
                    recycleBitmap = bmp
                }
            }
        }
        if (recycleBitmap != null) {
            bitmaps.remove(recycleBitmap)
        }
        HumbleLogs.log("BitmapPool found=%s for width=%s, height=%s.",
                recycleBitmap.toString(), width.toString(), height.toString())
        return recycleBitmap
    }
}