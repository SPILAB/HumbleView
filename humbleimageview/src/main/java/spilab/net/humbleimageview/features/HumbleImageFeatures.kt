package spilab.net.humbleimageview.features

import android.widget.ImageView
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.HumbleViewModel
import spilab.net.humbleimageview.model.ViewSize
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleImageFeatures(private val humbleImageView: HumbleImageView,
                                   private val model: HumbleViewModel) : DrawableEventsListener {

    private val drawableRecycler: DrawableRecycler
    private val featureTransition: FeatureTransition

    init {
        model.drawableEventsListener = this
        drawableRecycler = DrawableRecycler()
        featureTransition = FeatureTransition(humbleImageView, drawableRecycler)
    }

    fun onAttachedToWindow() {
        model.updateImageIfNeeded()
    }

    fun onDetachedFromWindow(imageViewDrawables: Array<ImageViewDrawable>) {
        featureTransition.onDetached()
        model.cancel()
        for (index in 0 until imageViewDrawables.size) {
            val imageViewDrawable = imageViewDrawables[index]
            drawableRecycler.recycleImageViewDrawable(imageViewDrawable)
        }
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
    fun configureFromImageView(imageView: ImageView,
                               imageViewDrawables: Array<ImageViewDrawable>?) {
        // Warning: imageViewDrawables can be null, because the
        // constructor of ImageView call override methods
        if (imageViewDrawables != null) {
            for (index in 0 until imageViewDrawables.size) {
                val iv = imageViewDrawables[index]
                iv.configureFromImageView()
            }
        }
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
}