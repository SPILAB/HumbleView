package spilab.net.humbleimageview.presenter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.HumbleViewModel
import spilab.net.humbleimageview.model.ViewSize
import spilab.net.humbleimageview.model.bitmap.BitmapPool
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleViewPresenter(private val humbleViewImage: HumbleImageView,
                                   private val model: HumbleViewModel) : DrawableEventsListener {

    init {
        model.drawableEventsListener = this
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

    fun setUrl(url: String?) {
        model.url = url
    }

    fun getUrl(): String? {
        return model.url
    }

    fun setOfflineCache(offlineCache: Boolean) {
        model.offlineCache = offlineCache
    }

    fun setLoadedImageScaleType(loadedScaleType: ImageView.ScaleType) {
        // TODO
    }

    fun setDebug(boolean: Boolean) {
        model.debug = boolean
    }

    fun getDebug(): Boolean {
        return model.debug
    }

    fun setViewSize(lastKnowSize: ViewSize) {
        model.viewSize = lastKnowSize
    }

    /**
     * Synchronize with the exact current state of the ImageView
     * Must be call each time the drawable is set
     * And each time the view is attached
     */
    fun synchronizeCurrentImageViewDrawables(imageViewDrawables: Array<ImageViewDrawable>?,
                                             drawable: Drawable?,
                                             alpha: Float) {
        // Warning: imageViewDrawables can be null, because the
        // constructor of ImageView call override methods
        if (imageViewDrawables != null) {
            recycleImageViewDrawable(imageViewDrawables[HumbleImageView.CURRENT_IDX])
            imageViewDrawables[HumbleImageView.CURRENT_IDX].mDrawable = drawable
            imageViewDrawables[HumbleImageView.CURRENT_IDX].mDrawable?.mutate()
            imageViewDrawables[HumbleImageView.CURRENT_IDX].mDrawable?.alpha = (alpha * 255.0f).toInt()
        }
    }

    fun recycleImageViewDrawable(recyclableDrawable: ImageViewDrawable) {
        val recyclableBitmap = (recyclableDrawable.mDrawable as? HumbleBitmapDrawable)?.bitmap
        recyclableDrawable.mDrawable = null
        if (recyclableBitmap != null) {
            BitmapPool.put(recyclableBitmap)
        }
    }

    override fun isCurrentOrNextDrawableIdEqualTo(humbleResourceId: HumbleResourceId): Boolean {
        return humbleViewImage.isCurrentOrNextDrawableId(humbleResourceId)
    }

    override fun onDrawableReady(drawable: HumbleBitmapDrawable) {
        humbleViewImage.addTransition(drawable)
    }
}