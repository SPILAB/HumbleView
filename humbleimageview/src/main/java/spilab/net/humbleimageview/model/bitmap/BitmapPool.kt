package spilab.net.humbleimageview.model.bitmap

import android.graphics.Bitmap
import spilab.net.humbleimageview.log.HumbleLogs
import java.lang.ref.WeakReference

internal object BitmapPool {

    private val bitmaps = mutableListOf<WeakReference<Bitmap>>()

    @Synchronized
    fun put(bitmap: Bitmap) {
        bitmaps.add(WeakReference(bitmap))
    }

    @Synchronized
    fun find(width: Int, height: Int): Bitmap? {
        var recycleBitmap: Bitmap? = null
        var recycleIterator: MutableIterator<WeakReference<Bitmap>>? = null
        val iterator = bitmaps.iterator()
        while (iterator.hasNext()) {
            val bmp = iterator.next().get()
            if (bmp == null) {
                iterator.remove()
            } else {
                if (bmp.width >= width && bmp.height >= height) {
                    if (recycleBitmap == null || recycleBitmap.byteCount > bmp.byteCount) {
                        recycleBitmap = bmp
                        recycleIterator = iterator
                    }
                }
            }
        }
        if (recycleIterator != null) {
            recycleIterator.remove()
        }
        HumbleLogs.log("BitmapPool recycled=$recycleBitmap for width=$width, height=$height.")
        return recycleBitmap
    }
}