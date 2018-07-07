package spilab.net.humbleimageview.presenter

import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal interface DrawableEventsListener {

    fun onDrawableReady(drawable: HumbleBitmapDrawable)

    fun isCurrentOrNextDrawableIdEqualTo(humbleResourceId: HumbleResourceId): Boolean
}