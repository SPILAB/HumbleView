package spilab.net.humbleimageview.features.transition

import android.support.v4.view.ViewCompat
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.features.memory.DrawableRecycler
import spilab.net.humbleimageview.model.LoadedImageScaleType
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class FeatureTransition(private val imageViewDrawables: HumbleImageView,
                                 private val drawableRecycler: DrawableRecycler) : CrossFadeTransition.HumbleTransitionListener {

    private var humbleTransition: CrossFadeTransition? = null

    fun addTransition(drawable: HumbleBitmapDrawable,
                      loadedImageScaleType: LoadedImageScaleType) {
        if (ViewCompat.isAttachedToWindow(imageViewDrawables)) {
            humbleTransition = CrossFadeTransition(imageViewDrawables,
                    imageViewDrawables.imageViewDrawables,
                    drawable,
                    loadedImageScaleType,
                    this)
        }
    }

    fun completeAnimation() {
        humbleTransition?.completeAnimation()
    }

    fun prepareOnDraw() {
        humbleTransition?.prepareOnDraw()
    }

    override fun onTransitionCompleted() {
        this.humbleTransition = null
        drawableRecycler.recycleImageViewDrawable(imageViewDrawables.imageViewDrawables[HumbleImageView.NEXT_IDX])
    }
}