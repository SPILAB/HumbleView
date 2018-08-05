package spilab.net.humbleimageview.features.transition

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.support.v7.graphics.Palette
import spilab.net.humbleimageview.android.AndroidPalette
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable

internal class PaletteTransition(private val imageViewDrawables: Array<ImageViewDrawable>,
                                 private val androidPalette: AndroidPalette = AndroidPalette()) : Transition {

    private lateinit var task: AsyncTask<Bitmap, Void, Palette>

    init {
        with(imageViewDrawables[Transition.CURRENT_IDX].getDrawable() as HumbleBitmapDrawable) {
            task = androidPalette.generate(this.bitmap) {
                imageViewDrawables[Transition.CURRENT_IDX].setDrawable(
                        ColorDrawable(it.mutedSwatch?.rgb ?: Color.GRAY)
                )
            }
        }
    }

    override fun prepareOnDraw() {}

    override fun cancel() {
        task.cancel(true)
    }
}