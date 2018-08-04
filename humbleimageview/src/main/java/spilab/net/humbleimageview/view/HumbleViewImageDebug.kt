package spilab.net.humbleimageview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import spilab.net.humbleimageview.features.transition.CrossFadeTransition


internal class HumbleViewImageDebug(context: Context) {

    private val background = Paint()
    private val textPaint = Paint()

    private val fontScale = context.resources.displayMetrics.scaledDensity
    private val densityScale = context.resources.displayMetrics.density
    private var textBounds = Rect()

    private var padding: Float

    fun onDraw(canvas: Canvas, sampleSize: Int) {
        var bottom = 0.0f
        if (sampleSize != -1) {
            bottom = drawTextDebug(canvas, sampleSize.toString(), padding, padding)
        }
        /*
        if (humbleTransition != null) {
            drawTextDebug(canvas, "a", padding, padding + bottom)
        }
        */
    }

    private inline fun drawTextDebug(canvas: Canvas, text: String, posX: Float, posY: Float): Float {
        // see: https://chris.banes.me/2014/03/27/measuring-text/
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val textWidth = textPaint.measureText(text)
        val textHeight = textBounds.height() + (padding * 2.0f)
        canvas.drawRect(posX, posY,
                textWidth + (padding * 2.0f), posY + textHeight, background)
        canvas.drawText(text, (posX + padding) - (textWidth / 2.0f),
                (posY + padding) + (textHeight / 2.0f), textPaint)
        return posY + textHeight
    }

    init {
        padding = 8.0f * densityScale
        background.apply {
            color = Color.DKGRAY
            style = Paint.Style.FILL
        }
        textPaint.apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 16.0f * fontScale
            isAntiAlias = true
        }
    }
}
