package spilab.net.humbleviewimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import spilab.net.humbleviewimage.model.HumbleViewDownloader
import spilab.net.humbleviewimage.view.AnimationTimer


class HumbleViewImage : AppCompatImageView {

    companion object {
        var DEFAULT_FADING_TIME_MILLIS = 500L;
    }

    private var url: String? = null

    private var nextDrawable: BitmapDrawable? = null

    private val downloader: HumbleViewDownloader by lazy { HumbleViewDownloader(this, url!!) }

    private val fadingAnimationTime: AnimationTimer by lazy { AnimationTimer(DEFAULT_FADING_TIME_MILLIS) }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        applyCustomAttributes(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyCustomAttributes(context, attrs, defStyleAttr)
    }

    private fun applyCustomAttributes(context: Context, attrs: AttributeSet?,
                                      defStyleAttr: Int) {
        val styledAttributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.HumbleViewImage, defStyleAttr, 0)

        try {
            url = styledAttributes.getString(R.styleable.HumbleViewImage_url)
        } finally {
            styledAttributes.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (url != null) {
            downloader.start()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (nextDrawable != null && canvas != null) {
            val (savedBitmap, fadingAlpha) = prepareNextDrawable()
            super.onDraw(canvas)
            if (fadingAlpha == 255) {
                nextDrawable = null
            } else {
                setImageDrawable(savedBitmap)
            }
        }
    }

    private fun prepareNextDrawable(): Pair<Drawable, Int> {
        val savedBitmap = drawable
        val fadingAlpha = (fadingAnimationTime.getNormalized(255.0f)).toInt()
        nextDrawable?.alpha = fadingAlpha
        setImageDrawable(nextDrawable)
        return Pair(savedBitmap, fadingAlpha)
    }

    internal fun transitionTo(bitmap: Bitmap) {
        nextDrawable = BitmapDrawable(context.resources, bitmap)
        fadingAnimationTime.start()
        invalidate()
    }
}