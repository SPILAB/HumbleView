package spilab.net.humbleimageview.features.transition.scale

import android.widget.ImageView
import kotlin.reflect.KProperty

class ScaleImageViewDelegate(private val imageView: ImageView) : ScaleDelegate {

    override fun getValue(thisRef: Any?, property: KProperty<*>): ImageView.ScaleType {
        return imageView.scaleType
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: ImageView.ScaleType) {
        imageView.scaleType = value
    }
}