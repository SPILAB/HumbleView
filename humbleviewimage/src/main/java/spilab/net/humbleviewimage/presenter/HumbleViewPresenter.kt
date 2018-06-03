package spilab.net.humbleviewimage.presenter

import android.graphics.drawable.Drawable
import android.util.Log
import spilab.net.humbleviewimage.HumbleViewImage
import spilab.net.humbleviewimage.android.ImageViewDrawable
import spilab.net.humbleviewimage.model.*
import spilab.net.humbleviewimage.model.bitmap.BitmapPool
import spilab.net.humbleviewimage.model.drawable.DrawableDecoder
import spilab.net.humbleviewimage.model.drawable.DrawableFromResourcePool
import spilab.net.humbleviewimage.model.drawable.DrawableResource
import spilab.net.humbleviewimage.model.drawable.HumbleBitmapDrawable

internal class HumbleViewPresenter(val humbleViewImage: HumbleViewImage) {

    var model: HumbleViewModel = HumbleViewModel(
            this,
            DrawableDecoder(
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

    fun updateDrawableResourceViewSize(drawable: Drawable?, viewSize: ViewSize) {
        if (drawable is DrawableResource) {
            drawable.viewSize = viewSize
        }
    }

    fun recycleDrawable(recyclableDrawable: Drawable?) {
        if (recyclableDrawable is DrawableResource) {
            DrawableFromResourcePool.put(recyclableDrawable)
        } else if (recyclableDrawable is HumbleBitmapDrawable) {
            BitmapPool.put(recyclableDrawable.bitmap)
        }
    }

    fun getRecycledDrawableResource(resId: Int, viewSize: ViewSize, alpha: Float): DrawableResource? {
        val drawable = DrawableFromResourcePool.find(resId, viewSize)
        drawable?.alpha = (alpha * 255.0f).toInt()
        return drawable
    }
}