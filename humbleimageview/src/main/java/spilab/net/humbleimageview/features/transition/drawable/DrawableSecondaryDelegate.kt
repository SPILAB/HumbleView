package spilab.net.humbleimageview.features.transition.drawable

import android.graphics.drawable.Drawable
import kotlin.reflect.KProperty

internal class DrawableSecondaryDelegate() : DrawableDelegate {

    private var drawable: Drawable? = null

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Drawable? {
        return drawable
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Drawable?) {
        drawable = value
    }
}