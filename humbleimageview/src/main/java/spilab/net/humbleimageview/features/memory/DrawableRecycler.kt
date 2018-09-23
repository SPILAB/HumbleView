package spilab.net.humbleimageview.features.memory

import android.widget.ImageView
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable

internal class DrawableRecycler {

    fun recycleImageViewDrawable(recyclableDrawable: AndroidImageViewDrawable) {
        val recyclableBitmap = (recyclableDrawable.getDrawable() as? HumbleBitmapDrawable)?.bitmap
        if (recyclableBitmap != null) {
            recyclableDrawable.setDrawable(null)
            BitmapPool.put(recyclableBitmap)
        }
    }

    fun recycleImageView(imageView: ImageView) {
        val recyclableDrawable = (imageView.drawable as? VectorDrawableFromResId)
        if (recyclableDrawable != null) {
            VectorDrawablePool.put(recyclableDrawable)
        }
    }
}