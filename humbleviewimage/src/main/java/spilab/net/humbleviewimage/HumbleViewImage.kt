package spilab.net.humbleviewimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import spilab.net.humbleviewimage.model.HumbleViewDownloader
import spilab.net.humbleviewimage.view.NextDrawable


class HumbleViewImage : AppCompatImageView {

    private val downloader: HumbleViewDownloader by lazy { HumbleViewDownloader(this) }
    private var nextDrawable: NextDrawable? = null

    var url: String? = null
        set(value) {
            field = value
            if (field != null) {
                downloader.start(field!!)
            }
            cancelNextDrawableTransition()
            Log.d("TAG", "url=$url")
        }

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (nextDrawable != null && canvas != null) {
            nextDrawable!!.prepareNextDrawable(this)
            super.onDraw(canvas)
            if (nextDrawable!!.isAnimationCompleted()) {
                nextDrawable = null
            } else {
                nextDrawable!!.restoreCurrentDrawable(this)
            }
        }
    }

    internal fun transitionTo(downloadedUrl: String, bitmap: Bitmap) {
        if (downloadedUrl == url) {
            nextDrawable = NextDrawable(this, bitmap)
            if (ViewCompat.isAttachedToWindow(this)) {
                Log.d("TAG", "transitionTo attached")
                invalidate()
            }
            else {
                Log.d("TAG", "transitionTo not attached")
            }
        }
    }

    private inline fun cancelNextDrawableTransition() {
        nextDrawable = null
    }
}