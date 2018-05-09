package spilab.net.humbleviewimage.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable

internal class HumbleBitmapDrawable(bitmap: Bitmap,
                                    val humbleBitmapId: HumbleBitmapId,
                                    res: Resources,
                                    val sampleSize: Int) : BitmapDrawable(res, bitmap)