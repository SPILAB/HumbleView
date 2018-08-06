package spilab.net.humbleimageview.features.transition.drawable

import android.graphics.drawable.Drawable
import kotlin.reflect.KProperty

interface DrawableDelegate {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Drawable?

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Drawable?)
}