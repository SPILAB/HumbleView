package spilab.net.humbleimageview.drawable

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import spilab.net.humbleimageview.features.request.ResourceId

internal class HumbleBitmapDrawable(bitmap: Bitmap,
                                    val resourceId: ResourceId,
                                    res: Resources,
                                    val sampleSize: Int) : BitmapDrawable(res, bitmap)