package spilab.net.humbleimageview.features

import android.graphics.drawable.Drawable
import android.os.Handler
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.AndroidViewCompat
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.features.memory.VectorDrawablePool
import spilab.net.humbleimageview.features.request.DrawableEventsListener
import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.features.sizelist.UrlsWithSizes
import spilab.net.humbleimageview.features.slideshow.SlideshowFactory
import spilab.net.humbleimageview.features.slideshow.SlideshowUrls
import spilab.net.humbleimageview.features.transform.BitmapTransformationFactory
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.view.ViewSize

internal class HumbleImageFeatures(private val humbleImageView: HumbleImageView,
                                   private val request: HumbleViewRequest = HumbleViewRequest(
                                           humbleImageView.context.applicationContext,
                                           humbleImageView.resources,
                                           Handler(humbleImageView.context.mainLooper)
                                   ),
                                   private val featureTransition: FeatureTransition = FeatureTransition(humbleImageView),
                                   private val slideshowFactory: SlideshowFactory = SlideshowFactory(),
                                   private val androidViewCompat: AndroidViewCompat = AndroidViewCompat()) : DrawableEventsListener {

    private val drawableRecycler: DrawableRecycler
    private var slideShow: SlideshowUrls? = null

    init {
        request.drawableEventsListener = this
        drawableRecycler = DrawableRecycler()
    }

    fun onAttachedToWindow() {
        request.requestImageIfNeeded()
        featureTransition.onAttached()
        slideShow?.onAttached()
    }

    fun onDetachedFromWindow() {
        slideShow?.onDetached()
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
        if (url != null) {
            slideShow?.cancel()
            slideShow = null
            request.urls = UrlsWithSizes.fromUrl(url)
        }
    }

    fun setUrls(urls: UrlsWithSizes) {
        request.urls = urls
    }

    fun setSlideshowUrls(urls: Array<out CharSequence>?) {
        if (urls != null) {
            slideShow = slideshowFactory.fromUrls(request, featureTransition, androidViewCompat.isAttachedToWindow(humbleImageView), urls)
        }
    }

    fun setOfflineCache(offlineCache: Boolean) {
        request.offlineCache = offlineCache
    }

    fun setViewSize(lastKnowSize: ViewSize) {
        request.viewSize = lastKnowSize
    }

    fun setTransform(className: String?, values: String?) {
        if (className != null) {
            request.bitmapTransform = BitmapTransformationFactory
                    .createInstance(className, values ?: "")
        }
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
        for (index in 0 until imageViewDrawables.size) {
            val iv = imageViewDrawables[index]
            iv.configureFromImageView()
        }
    }

    private fun configureImageViewDrawable() {
        val drawable = humbleImageView.drawable
        if (drawable is VectorDrawableFromResId) {
            drawable.width = humbleImageView.width
            drawable.height = humbleImageView.height
        }
    }

    fun setDelayBetweenLoadedImagesMillis(delayBetweenLoadedImagesMillis: Long) {
        slideShow?.delayBetweenLoadedImagesMillis = delayBetweenLoadedImagesMillis
    }
}