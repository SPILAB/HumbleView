package spilab.net.humbleviewimage.model.drawable


import android.graphics.drawable.Drawable
import spilab.net.humbleviewimage.android.DrawableWrapper
import spilab.net.humbleviewimage.model.ViewSize

internal class DrawableResource(drawable: Drawable,
                                val resId: Int,
                                var viewSize: ViewSize = ViewSize()) : DrawableWrapper(drawable)