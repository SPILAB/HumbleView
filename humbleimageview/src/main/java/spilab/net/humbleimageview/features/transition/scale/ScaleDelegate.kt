package spilab.net.humbleimageview.features.transition.scale

import android.content.res.TypedArray
import android.widget.ImageView
import spilab.net.humbleimageview.R
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import kotlin.reflect.KProperty

internal class ScaleDelegate(private val imageView: ImageView) {

    companion object {
        const val SCALE_TYPE_UNSET = -1
    }

    private var loadedScaleType = ScaleType(false, AndroidImageViewDrawable.DEFAUL_SCALE_TYPE)

    data class ScaleType(val useLoadedScale: Boolean, val scaleType: ImageView.ScaleType)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): ImageView.ScaleType {
        val isHumbleBitmapDrawable = thisRef is AndroidImageViewDrawable && thisRef.getDrawable() is HumbleBitmapDrawable
        return if (loadedScaleType.useLoadedScale && isHumbleBitmapDrawable) {
            loadedScaleType.scaleType
        } else {
            imageView.scaleType
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ImageView.ScaleType) {
        loadedScaleType = ScaleType(false, value)
        imageView.scaleType = value
    }

    fun setLoadedScaleType(value: ImageView.ScaleType) {
        loadedScaleType = ScaleType(true, value)
    }

    fun initLoadedScaleType(styledAttributes: TypedArray) {
        val scaleTypeAttribute = styledAttributes.getInteger(R.styleable.HumbleImageView_loadedImageScaleType, ScaleDelegate.SCALE_TYPE_UNSET)
        if (scaleTypeAttribute != ScaleDelegate.SCALE_TYPE_UNSET) {
            loadedScaleType = ScaleType(true, getLoadedScaleType(scaleTypeAttribute))
        }
    }

    private fun getLoadedScaleType(scaleType: Int): ImageView.ScaleType {
        when (scaleType) {
            0 -> return ImageView.ScaleType.MATRIX
            1 -> return ImageView.ScaleType.FIT_XY
            2 -> return ImageView.ScaleType.FIT_START
            3 -> return ImageView.ScaleType.FIT_CENTER
            4 -> return ImageView.ScaleType.FIT_END
            5 -> return ImageView.ScaleType.CENTER
            6 -> return ImageView.ScaleType.CENTER_CROP
            7 -> return ImageView.ScaleType.CENTER_INSIDE
        }
        return ImageView.ScaleType.FIT_CENTER
    }
}