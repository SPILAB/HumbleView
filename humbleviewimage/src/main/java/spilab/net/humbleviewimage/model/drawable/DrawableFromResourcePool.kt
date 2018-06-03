package spilab.net.humbleviewimage.model.drawable

import spilab.net.humbleviewimage.model.ViewSize
import java.lang.ref.WeakReference

internal object DrawableFromResourcePool {

    private val drawables = mutableListOf<WeakReference<DrawableResource>>()

    fun put(drawableFromResource: DrawableResource) {
        drawables.add(WeakReference(drawableFromResource))
    }

    fun find(resId: Int, viewSize: ViewSize): DrawableResource? {
        var recycleDrawable: DrawableResource? = null
        val iterator = drawables.iterator()
        while (iterator.hasNext()) {
            val drawable = iterator.next().get()
            if (drawable == null) {
                iterator.remove()
            } else {
                if (drawable.resId == resId && drawable.viewSize == viewSize) {
                    recycleDrawable = drawable
                    iterator.remove()
                    break
                }
            }
        }
        return recycleDrawable
    }
}