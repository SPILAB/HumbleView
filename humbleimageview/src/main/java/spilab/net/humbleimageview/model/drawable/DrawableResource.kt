package spilab.net.humbleimageview.model.drawable


import android.graphics.drawable.Drawable
import spilab.net.humbleimageview.android.DrawableWrapper
import spilab.net.humbleimageview.model.ViewSize

internal class DrawableResource(drawable: Drawable,
                                val resId: Int,
                                var viewSize: ViewSize = ViewSize()) : DrawableWrapper(drawable)