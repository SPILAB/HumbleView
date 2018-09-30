package spilab.net.humbleimageview.features.transform

import android.graphics.*

/**
 * Adapted from the original source code:
 * https://gist.github.com/aprock/6213395
 */
class RoundedCornerBitmapTransformation : BitmapTransformation {

    private val radius = 32.0f
    private val margin = 8.0f

    override fun setValues(string: String) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawRoundRect(RectF(margin, margin, source.width - margin, source.height - margin), radius, radius, paint)

        if (source !== output) {
            source.recycle()
        }

        return output
    }
}