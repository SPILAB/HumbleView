package spilab.net.humbleimageview.features.transition.scale

import android.content.res.TypedArray
import android.widget.ImageView
import spilab.net.humbleimageview.R
import spilab.net.humbleimageview.android.ImageViewDrawable
import kotlin.reflect.KProperty

class ScaleSecondaryDelegate(private val imageView: ImageView) : ScaleDelegate {

    companion object {
        const val SCALE_TYPE_UNSET = -1
    }

    private var scaleType = ScaleType(true, ImageViewDrawable.DEFAUL_SCALE_TYPE)

    data class ScaleType(val useImageViewScale: Boolean, val scaleType: ImageView.ScaleType)

    override fun getValue(thisRef: Any?, property: KProperty<*>): ImageView.ScaleType {
        return if (scaleType.useImageViewScale) {
            imageView.scaleType
        } else {
            scaleType.scaleType
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: ImageView.ScaleType) {
        scaleType = ScaleType(false, value)
    }

    fun setScaleType(value: ImageView.ScaleType) {
        scaleType = ScaleType(false, value)
    }

    fun initScaleType(styledAttributes: TypedArray) {
        val scaleTypeAttribute = styledAttributes.getInteger(R.styleable.HumbleImageView_loadedImageScaleType, ScaleSecondaryDelegate.SCALE_TYPE_UNSET)
        if (scaleTypeAttribute != ScaleSecondaryDelegate.SCALE_TYPE_UNSET) {
            scaleType = ScaleType(false, getLoadedScaleType(scaleTypeAttribute))
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