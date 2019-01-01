package spilab.net.humbleimageview.features.transition

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.memory.DrawableRecycler

internal class PaletteTransition(private val imageViewDrawables: Array<AndroidImageViewDrawable>,
                                 private val transitionListener: TransitionListener,
                                 private val androidPalette: AndroidPalette = AndroidPalette(),
                                 private val drawableRecycler: DrawableRecycler = DrawableRecycler()) : TransitionDrawable() {

    private var task: AsyncTask<Bitmap, Void, androidx.palette.graphics.Palette>? = null

    override fun start() {
        with(imageViewDrawables[Transition.CURRENT_IDX].getDrawable() as HumbleBitmapDrawable) {
            task = androidPalette.generate(this.bitmap) {
                updateDrawable {
                    drawableRecycler.recycleImageViewDrawable(imageViewDrawables[Transition.CURRENT_IDX])
                    drawableRecycler.recycleImageViewDrawable(imageViewDrawables[Transition.NEXT_IDX])
                    imageViewDrawables[Transition.CURRENT_IDX].setDrawable(
                            ColorDrawable(it.mutedSwatch?.rgb ?: Color.GRAY)
                    )
                }
                transitionListener.onTransitionCompleted()
            }
        }
    }

    override fun prepareOnDraw() {}

    override fun onAttached() {
        // We stop this transition on attach to keep the bitmap
        cancel()
    }

    override fun onDetached() {
        cancel()
    }

    override fun cancel() {
        task?.cancel(true)
        transitionListener.onTransitionCompleted()
    }
}