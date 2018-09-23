package spilab.net.humbleimageview.features.request

import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.view.ViewSize
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.sizelist.UrlWithSize

internal data class ResourceId(val urlWithSize: UrlWithSize, val viewSize: ViewSize) {

    fun isPresentIn(imageViewDrawables: Array<AndroidImageViewDrawable>): Boolean {
        for (index in 0 until imageViewDrawables.size) {
            val drawable = imageViewDrawables[index].getDrawable()
            if (drawable is HumbleBitmapDrawable && drawable.resourceId == this) {
                return true
            }
        }
        return false
    }
}