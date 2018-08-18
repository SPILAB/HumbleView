package spilab.net.humbleimageview.features.memory

import spilab.net.humbleimageview.log.HumbleLogs
import java.util.*

internal object VectorDrawablePool {

    private val drawables = WeakHashMap<VectorDrawableFromResId, Boolean>()

    @Synchronized
    fun put(vectorDrawableFromResId: VectorDrawableFromResId) {
        if (vectorDrawableFromResId.width != 0 && vectorDrawableFromResId.height != 0) {
            HumbleLogs.log("VectorDrawablePool put=%s.", vectorDrawableFromResId.toString())
            drawables[vectorDrawableFromResId] = true
        } else {
            HumbleLogs.log("VectorDrawablePool don't put=%s.", vectorDrawableFromResId.toString())
        }
    }

    @Synchronized
    fun find(resId: Int, width: Int, height: Int): VectorDrawableFromResId? {
        HumbleLogs.log("VectorDrawablePool searching in %s elements for resId=%s width=%s, height=%s.",
                drawables.size.toString(), resId.toString(),
                width.toString(), height.toString())
        var recycleBitmap: VectorDrawableFromResId? = null
        for (drawable in drawables) {
            if (drawable.key.resId == resId
                    && drawable.key.width == width && drawable.key.height == height) {
                recycleBitmap = drawable.key
                break
            }
        }
        if (recycleBitmap != null) {
            drawables.remove(recycleBitmap)
        }
        HumbleLogs.log("VectorDrawablePool found=%s for resId=%s width=%s, height=%s.",
                recycleBitmap.toString(), resId.toString(), width.toString(), height.toString())
        return recycleBitmap
    }

    @Synchronized
    fun clear() {
        drawables.clear()
    }
}

