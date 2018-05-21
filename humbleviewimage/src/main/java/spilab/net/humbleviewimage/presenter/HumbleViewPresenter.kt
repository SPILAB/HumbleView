package spilab.net.humbleviewimage.presenter

import android.util.Log
import spilab.net.humbleviewimage.HumbleViewImage
import spilab.net.humbleviewimage.model.*

internal class HumbleViewPresenter(val humbleViewImage: HumbleViewImage) {

    var model: HumbleViewModel = HumbleViewModel(
            this,
            BitmapDrawableDecoder(
                    resources = humbleViewImage.resources),
            context = humbleViewImage.context.applicationContext)

    fun isCurrentOrNextDrawableId(bitmapId: HumbleBitmapId): Boolean {
        return humbleViewImage.isCurrentOrNextDrawableId(bitmapId)
    }

    fun start() {
        model.updateImageIfNeeded()
    }

    fun stop() {
        model.cancelDownloadIfNeeded()
        humbleViewImage.completeAnimation()
    }

    fun addTransitionDrawable(drawable: HumbleBitmapDrawable) {
        Log.d(HumbleViewConfig.TAG, "add transition to view=$humbleViewImage with drawable id=${drawable.humbleBitmapId}")
        humbleViewImage.addTransition(drawable)
    }
}