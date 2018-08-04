package spilab.net.humbleimageview.features

import android.content.res.TypedArray
import android.widget.ImageView
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.HumbleViewModel
import spilab.net.humbleimageview.model.LoadedImageScaleType
import spilab.net.humbleimageview.model.ViewSize
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class HumbleImageFeatures(private val humbleImageView: HumbleImageView,
                                   private val model: HumbleViewModel,
                                   imageViewDrawables: Array<ImageViewDrawable>) : DrawableEventsListener {

    private val drawableRecycler: DrawableRecycler
    private val featureTransition: FeatureTransition

    init {
        model.drawableEventsListener = this
        drawableRecycler = DrawableRecycler()
        featureTransition = FeatureTransition(humbleImageView, drawableRecycler)
    }

    private lateinit var loadedImageScaleType: LoadedImageScaleType

    fun initLoadedImageScaleType(styledAttributes: TypedArray) {
        loadedImageScaleType = LoadedImageScaleType(styledAttributes)
    }

    fun onAttachedToWindow() {
        model.updateImageIfNeeded()
    }

    fun onDetachedFromWindow(imageViewDrawables: Array<ImageViewDrawable>) {
        featureTransition.completeAnimation()
        model.cancel()
        for (imageViewDrawable in imageViewDrawables) {
            drawableRecycler.recycleImageViewDrawable(imageViewDrawable)
        }
    }

    fun onPause() {
        model.cancel()
    }

    fun onResume() {
        // not implemented
    }

    fun setUrl(url: String?) {
        model.url = url
    }

    fun setOfflineCache(offlineCache: Boolean) {
        model.offlineCache = offlineCache
    }

    fun setLoadedImageScaleType(scaleType: ImageView.ScaleType) {
        loadedImageScaleType.setLoadedImageScaleType(scaleType)
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
            for (index in 0 until imageViewDrawables!!.size) {
                val iv = imageViewDrawables!![index]
                iv.configureFromImageView(loadedImageScaleType.getScaleType(imageView, iv.getDrawable()))
            }
        }
    }

    /**
     * Synchronize with the exact current state of the ImageView
     * Must be call each time the drawable is set
     * And each time the view is attached
     */
    fun synchronizeCurrentImageViewDrawables(imageView: ImageView,
                                             imageViewDrawables: Array<ImageViewDrawable>?,
                                             alpha: Float) {
        // Warning: imageViewDrawables can be null, because the
        // constructor of ImageView call override methods
        if (imageViewDrawables != null) {
            drawableRecycler.recycleImageViewDrawable(imageViewDrawables[HumbleImageView.CURRENT_IDX])
            imageViewDrawables[HumbleImageView.CURRENT_IDX].setDrawable(imageView.drawable,
                    loadedImageScaleType.getScaleType(imageView, imageView.drawable))
            imageViewDrawables[HumbleImageView.CURRENT_IDX].getDrawable()?.mutate()
            imageViewDrawables[HumbleImageView.CURRENT_IDX].getDrawable()?.alpha = (alpha * 255.0f).toInt()
        }
    }

    override fun isCurrentOrNextDrawableIdEqualTo(humbleResourceId: HumbleResourceId): Boolean {
        return humbleImageView.isCurrentOrNextDrawableId(humbleResourceId)
    }

    override fun onDrawableReady(drawable: HumbleBitmapDrawable) {
        featureTransition.addTransition(drawable, loadedImageScaleType)
        // humbleImageView.addTransition(drawable, loadedImageScaleType)
    }

    fun onTransitionCompleted(imageViewDrawable: ImageViewDrawable) {
        drawableRecycler.recycleImageViewDrawable(imageViewDrawable)
    }

    fun prepareOnDraw() {
        featureTransition?.prepareOnDraw()
    }
}