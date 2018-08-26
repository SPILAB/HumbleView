package spilab.net.humbleimageview.features

import android.graphics.drawable.Drawable
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.HumbleViewModel
import spilab.net.humbleimageview.model.ViewSize
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.features.memory.VectorDrawablePool
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleImageFeatures(private val humbleImageView: HumbleImageView,
                                   private val model: HumbleViewModel,
                                   private val featureTransition: FeatureTransition = FeatureTransition(humbleImageView)) : DrawableEventsListener {

    private val drawableRecycler: DrawableRecycler

    init {
        model.drawableEventsListener = this
        drawableRecycler = DrawableRecycler()
    }

    fun onAttachedToWindow() {
        model.updateImageIfNeeded()
        featureTransition.onAttached()
    }

    fun onDetachedFromWindow() {
        featureTransition.onDetached()
        model.cancel()
    }

    fun onPause() {
        featureTransition.onPause()
        model.cancel()
    }

    fun onResume() {
        model.updateImageIfNeeded()
    }

    fun setUrl(url: String?) {
        model.url = url
    }

    fun setOfflineCache(offlineCache: Boolean) {
        model.offlineCache = offlineCache
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
     * Must be call each time the view bounds change
     */
    fun configureFromImageView() {
        // Warning: imageViewDrawables can be null, because the
        // constructor of ImageView call override methods
        if (humbleImageView != null) {
            configureImageViewDrawables()
            configureImageViewDrawable()
        }
    }

    fun drawableReplaced() {
        featureTransition.drawableReplaced()
    }

    fun getVectorDrawable(resId: Int, width: Int, height: Int): Drawable? {
        val drawableFromResId = VectorDrawablePool.find(resId, width, height)
        drawableFromResId?.drawable?.alpha = (humbleImageView.alpha * 255.0f).toInt()
        return drawableFromResId
    }

    override fun isCurrentOrNextDrawableIdEqualTo(humbleResourceId: HumbleResourceId): Boolean {
        return humbleImageView.isCurrentOrNextDrawableId(humbleResourceId)
    }

    override fun onDrawableReady(drawable: HumbleBitmapDrawable) {
        featureTransition.addTransition(drawable)
    }

    fun prepareOnDraw() {
        featureTransition.prepareOnDraw()
    }

    private fun configureImageViewDrawables() {
        val imageViewDrawables = humbleImageView.imageViewDrawables
        if (imageViewDrawables != null) {
            for (index in 0 until imageViewDrawables.size) {
                val iv = imageViewDrawables[index]
                iv.configureFromImageView()
            }
        }
    }

    private fun configureImageViewDrawable() {
        val drawable = humbleImageView.drawable
        if (drawable is VectorDrawableFromResId) {
            drawable.width = humbleImageView.width
            drawable.height = humbleImageView.height
        }
    }
}