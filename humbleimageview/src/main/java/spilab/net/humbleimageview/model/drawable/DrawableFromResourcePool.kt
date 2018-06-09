package spilab.net.humbleimageview.model.drawable

import android.graphics.drawable.Drawable
import spilab.net.humbleimageview.model.ViewSize
import java.util.*

internal object DrawableFromResourcePool {

    private val drawables = WeakHashMap<Drawable, DrawableFromResource>()

    inline fun put(drawable: Drawable, drawableFromResource: DrawableFromResource) {
        drawables[drawable] = drawableFromResource
    }

    fun find(resId: Int, viewSize: ViewSize): Drawable? {
        for (mutableEntry in drawables) {
            if (mutableEntry.value.resId == resId
                    && mutableEntry.value.viewSize == viewSize) {
                return mutableEntry.key
            }
        }
        return null
    }

    inline fun updateViewSize(mDrawable: Drawable?, viewSize: ViewSize) {
        drawables[mDrawable]?.viewSize = viewSize
    }
}