package spilab.net.humbleimageview.features.request

import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable

internal interface DrawableEventsListener {

    fun onDrawableReady(drawable: HumbleBitmapDrawable)

    fun isCurrentOrNextDrawableIdEqualTo(humbleResourceId: ResourceId): Boolean
}