package spilab.net.humbleimageview.model

import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal data class HumbleResourceId(val url: String, val viewSize: ViewSize) {

    fun isPresentIn(imageViewDrawables: Array<ImageViewDrawable>): Boolean {
        for (index in 0 until imageViewDrawables.size) {
            val drawable = imageViewDrawables[index].getDrawable()
            if (drawable is HumbleBitmapDrawable && drawable.humbleResourceId == this) {
                return true
            }
        }
        return false
    }
}