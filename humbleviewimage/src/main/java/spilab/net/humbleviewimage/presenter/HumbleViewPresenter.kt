package spilab.net.humbleviewimage.presenter

import android.content.Context
import android.content.res.Resources
import spilab.net.humbleviewimage.HumbleViewImage
import spilab.net.humbleviewimage.model.HumbleBitmapDrawable
import spilab.net.humbleviewimage.model.HumbleBitmapId
import spilab.net.humbleviewimage.model.HumbleViewModel
import spilab.net.humbleviewimage.view.HumbleTransition

internal class HumbleViewPresenter(val humbleViewImage: HumbleViewImage) {

    var model: HumbleViewModel = HumbleViewModel(this)

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

    fun getApplicationContext(): Context {
        return humbleViewImage.context.applicationContext
    }

    fun getResources(): Resources {
        return humbleViewImage.resources
    }

    fun start() {
        model.updateImageIfNeeded()
    }

    fun stop() {
        model.cancelDownloadIfNeeded()
        humbleViewImage.completeAnimation()
    }

    fun addTransitionDrawable(drawable: HumbleBitmapDrawable) {
        humbleViewImage.addTransition(HumbleTransition(humbleViewImage, drawable))
    }
}