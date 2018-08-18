package spilab.net.humbleimageview.features.memory

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable

data class VectorDrawableFromResId(val drawable: Drawable, val resId: Int,
                                   var width: Int, var height: Int) : Drawable() {
    override fun draw(canvas: Canvas?) {
        drawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
    }

    override fun getOpacity(): Int = drawable.opacity

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable.colorFilter = colorFilter
    }
}
