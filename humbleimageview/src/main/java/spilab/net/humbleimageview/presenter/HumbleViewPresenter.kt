package spilab.net.humbleimageview.presenter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.content.res.AppCompatResources
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.*
import spilab.net.humbleimageview.model.bitmap.BitmapPool
import spilab.net.humbleimageview.model.drawable.DrawableDecoder
import spilab.net.humbleimageview.model.drawable.DrawableFromResource
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

    fun getImageResource(context: Context, resId: Int, lastKnowSize: ViewSize, alpha: Float): Drawable? {
        var drawable: Drawable? = null
        if (lastKnowSize.isValid()) {
            drawable = getRecycledDrawableResource(resId, lastKnowSize)
            drawable = drawable?.constantState?.newDrawable()?.mutate()
        }
        if (drawable == null) {
            drawable = AppCompatResources.getDrawable(context, resId)
            if (drawable != null) {
                DrawableFromResourcePool.put(drawable, DrawableFromResource(resId, lastKnowSize))
            }
        }
        drawable?.alpha = (alpha * 255.0f).toInt()
        return drawable
    }
}