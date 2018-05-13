package spilab.net.humbleviewimage.presenter

import android.util.Log
import spilab.net.humbleviewimage.HumbleViewImage
import spilab.net.humbleviewimage.model.*
import spilab.net.humbleviewimage.view.HumbleTransition

internal class HumbleViewPresenter(val humbleViewImage: HumbleViewImage) {

    var model: HumbleViewModel = HumbleViewModel(
            this,
            BitmapDrawableDecoder(
                    resources = humbleViewImage.resources),
            context = humbleViewImage.context.applicationContext)

    fun isCurrentOrNextDrawableId(bitmapId: HumbleBitmapId): Boolean {
        val currentDrawable = humbleViewImage.drawable
        if (currentDrawable is HumbleBitmapDrawable) {
            if (currentDrawable.humbleBitmapId == bitmapId) return true
        }
        if (humbleViewImage.humbleTransition != null &&
                humbleViewImage.humbleTransition!!.isBitmapId(bitmapId)) {
            return true
        }
        return false
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
        humbleViewImage.addTransition(HumbleTransition(humbleViewImage, drawable))
    }
}