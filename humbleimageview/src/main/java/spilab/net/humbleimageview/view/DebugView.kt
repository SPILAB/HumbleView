package spilab.net.humbleimageview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable


internal class DebugView(context: Context) {

    private var flags: Int = 0

    private val backgroundPaint = Paint()
    private val textPaint = Paint()

    private val fontScale = context.resources.displayMetrics.scaledDensity
    private val densityScale = context.resources.displayMetrics.density
    private var bounds = Rect()

    private var padding: Float

    init {
        padding = 8.0f * densityScale
        backgroundPaint.apply {
            color = Color.DKGRAY
            style = Paint.Style.FILL
        }
        textPaint.apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 12.0f * fontScale
            isAntiAlias = true
        }
    }

    fun setDebugFlags(flags: String) {
        setDebugFlags(DebugViewFlags.fromLayoutName(flags).value)
    }

    fun setDebugFlags(flags: Int) {
        this.flags = flags
    }

    fun onDraw(canvas: Canvas, humbleBitmapDrawable: HumbleBitmapDrawable) {
        if (flags > 0) {
            var position = 0.0f
            if (DebugViewFlags.DECODE_SIZE.isEnable(flags)) {
                position = drawTextDebug(canvas, humbleBitmapDrawable.sampleSize.toString(), 0.0f, position)
            }
            if (DebugViewFlags.REQUEST_VIEW_SIZE.isEnable(flags)) {
                drawTextDebug(canvas, humbleBitmapDrawable.resourceId.urlWithSize.urlSize.toString(), 0.0f, position)
            }
        }
    }

    private inline fun drawTextDebug(canvas: Canvas, text: String, posX: Float, posY: Float): Float {
        // see: https://chris.banes.me/2014/03/27/measuring-text/
        textPaint.getTextBounds(text, 0, text.length, bounds)
        bounds.offset(0, -bounds.top)
        canvas.drawRect(bounds, backgroundPaint)
        canvas.drawText(text, 0.0f, bounds.bottom.toFloat(), textPaint)
        return posY + bounds.bottom
    }
}
