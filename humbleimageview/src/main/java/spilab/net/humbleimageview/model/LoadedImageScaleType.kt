package spilab.net.humbleimageview.model

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.widget.ImageView
import spilab.net.humbleimageview.R
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class LoadedImageScaleType(styledAttributes: TypedArray) {

    companion object {
        const val SCALE_TYPE_UNSET = -1
    }

    data class LoadedScaleType(val useImageViewScale: Boolean, val scaleType: ImageView.ScaleType)

    private var currentLoadedScaleType = LoadedScaleType(true, ImageView.ScaleType.FIT_CENTER)

    init {
        val scaleType = styledAttributes.getInteger(R.styleable.HumbleImageView_loadedImageScaleType, SCALE_TYPE_UNSET)
        if (scaleType != SCALE_TYPE_UNSET) {
            setLoadedImageScaleType(getLoadedScaleType(scaleType))
        }
    }

    fun setLoadedImageScaleType(scaleType: ImageView.ScaleType) {
        currentLoadedScaleType = LoadedScaleType(false, scaleType)
    }

    fun getScaleType(imageView: ImageView, drawable: Drawable?): ImageView.ScaleType {
        if (drawable is HumbleBitmapDrawable && !currentLoadedScaleType.useImageViewScale) {
            return currentLoadedScaleType.scaleType
        }
        return imageView.scaleType
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