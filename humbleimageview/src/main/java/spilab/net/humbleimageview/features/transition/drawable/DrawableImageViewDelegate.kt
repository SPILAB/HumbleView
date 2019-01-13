package spilab.net.humbleimageview.features.transition.drawable

import android.graphics.drawable.Drawable
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import kotlin.reflect.KProperty

internal class DrawableImageViewDelegate(private val imageView: HumbleImageView) : DrawableDelegate {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Drawable? {
        if (imageView.drawable is VectorDrawableFromResId) {
            return (imageView.drawable as VectorDrawableFromResId).drawable
        }
        return imageView.drawable
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Drawable?) {
        imageView.setImageDrawableInternal(value)
    }
}