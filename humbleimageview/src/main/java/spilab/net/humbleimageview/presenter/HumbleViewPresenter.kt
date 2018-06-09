package spilab.net.humbleimageview.presenter

import android.graphics.drawable.Drawable
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.*
import spilab.net.humbleimageview.model.bitmap.BitmapPool
import spilab.net.humbleimageview.model.drawable.DrawableDecoder
import spilab.net.humbleimageview.model.drawable.DrawableFromResourcePool
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleViewPresenter(private val humbleViewImage: HumbleImageView) {

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
        humbleViewImage.addTransition(drawable)
    }

    fun recycleDrawable(recyclableDrawable: Drawable?) {
        if (recyclableDrawable is HumbleBitmapDrawable) {
            BitmapPool.put(recyclableDrawable.bitmap)
        }
    }

    fun getRecycledDrawableResource(resId: Int, viewSize: ViewSize): Drawable? {
        return DrawableFromResourcePool.find(resId, viewSize)
    }

    fun setViewSize(lastKnowSize: ViewSize, imageViewDrawables: Array<ImageViewDrawable>) {
        model.viewSize = lastKnowSize
        imageViewDrawables.forEach {
            DrawableFromResourcePool.updateViewSize(it.mDrawable, lastKnowSize)
        }
    }
}