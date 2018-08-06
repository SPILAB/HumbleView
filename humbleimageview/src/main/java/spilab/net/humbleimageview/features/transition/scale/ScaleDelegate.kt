package spilab.net.humbleimageview.features.transition.scale

import android.widget.ImageView
import kotlin.reflect.KProperty

interface ScaleDelegate {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): ImageView.ScaleType

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ImageView.ScaleType)
}