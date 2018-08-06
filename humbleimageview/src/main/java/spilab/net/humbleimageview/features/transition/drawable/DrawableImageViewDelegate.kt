package spilab.net.humbleimageview.features.transition.drawable

import android.graphics.drawable.Drawable
import android.widget.ImageView
import kotlin.reflect.KProperty

class DrawableImageViewDelegate(private val imageView: ImageView) : DrawableDelegate {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Drawable? {
        return imageView.drawable
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Drawable?) {
        imageView.setImageDrawable(value)
    }
}