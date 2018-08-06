package spilab.net.humbleimageview.features.memory

import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class DrawableRecycler {

    fun recycleImageViewDrawable(recyclableDrawable: ImageViewDrawable) {
        val recyclableBitmap = (recyclableDrawable.getDrawable() as? HumbleBitmapDrawable)?.bitmap
        recyclableDrawable.setDrawable(null)
        if (recyclableBitmap != null) {
            BitmapPool.put(recyclableBitmap)
        }
    }
}