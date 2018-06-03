package spilab.net.humbleviewimage.model.drawable

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import spilab.net.humbleviewimage.model.HumbleBitmapId

internal class HumbleBitmapDrawable(bitmap: Bitmap,
                                    val humbleBitmapId: HumbleBitmapId,
                                    res: Resources,
                                    val sampleSize: Int) : BitmapDrawable(res, bitmap)