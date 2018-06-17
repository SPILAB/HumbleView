package spilab.net.humbleimageview.presenter

import android.os.Handler
import android.os.Looper
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.*
import spilab.net.humbleimageview.model.bitmap.BitmapPool
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleViewPresenter(private val humbleViewImage: HumbleImageView) {

    var model: HumbleViewModel = HumbleViewModel(this,
            humbleViewImage.resources,
            Handler(Looper.getMainLooper()))

    fun isCurrentOrNextDrawableId(bitmapId: HumbleBitmapId): Boolean {
        return humbleViewImage.isCurrentOrNextDrawableId(bitmapId)
    }

    fun start() {
        model.updateImageIfNeeded()
    }

    fun stop(imageViewDrawables: Array<ImageViewDrawable>) {
        model.cancel()
        for (imageViewDrawable in imageViewDrawables) {
            recycleImageViewDrawable(imageViewDrawable)
        }
    }

    fun addTransitionDrawable(drawable: HumbleBitmapDrawable) {
        humbleViewImage.addTransition(drawable)
    }

    fun recycleImageViewDrawable(recyclableDrawable: ImageViewDrawable) {
        val recyclableBitmap = (recyclableDrawable.mDrawable as? HumbleBitmapDrawable)?.bitmap
        recyclableDrawable.mDrawable = null
        if (recyclableBitmap != null) {
            BitmapPool.put(recyclableBitmap)
        }
    }

    fun setViewSize(lastKnowSize: ViewSize) {
        model.viewSize = lastKnowSize
    }
}