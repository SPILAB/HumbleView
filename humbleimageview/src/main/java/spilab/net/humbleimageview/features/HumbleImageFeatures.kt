package spilab.net.humbleimageview.features

import android.graphics.drawable.Drawable
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.view.ViewSize
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.features.memory.VectorDrawablePool
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.request.DrawableEventsListener
import spilab.net.humbleimageview.features.sizelist.SizeList
import spilab.net.humbleimageview.features.sizelist.UrlWithSize

internal class HumbleImageFeatures(private val humbleImageView: HumbleImageView,
                                   private val request: HumbleViewRequest,
                                   private val featureTransition: FeatureTransition = FeatureTransition(humbleImageView)) : DrawableEventsListener {

    private val drawableRecycler: DrawableRecycler

    init {
        request.drawableEventsListener = this
        drawableRecycler = DrawableRecycler()
    }

    fun onAttachedToWindow() {
        request.requestImageIfNeeded()
        featureTransition.onAttached()
    }

    fun onDetachedFromWindow() {
        featureTransition.onDetached()
        request.cancel()
    }

    fun onPause() {
        featureTransition.onPause()
        request.cancel()
    }

    fun onResume() {
        request.requestImageIfNeeded()
    }

    fun setUrl(url: String?) {
        request.urls = SizeList.fromUrl(url)
    }

    fun setUrls(urls: SizeList) {
        request.urls = urls
    }

    fun setOfflineCache(offlineCache: Boolean) {
        request.offlineCache = offlineCache
    }

    fun setViewSize(lastKnowSize: ViewSize) {
        request.viewSize = lastKnowSize
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

    override fun isCurrentOrNextDrawableIdEqualTo(humbleResourceId: ResourceId): Boolean {
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