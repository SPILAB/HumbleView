package spilab.net.humbleviewimage

import android.content.Context
import android.graphics.Canvas
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import spilab.net.humbleviewimage.model.*
import spilab.net.humbleviewimage.view.HumbleViewImageDebug
import spilab.net.humbleviewimage.view.HumbleViewSize
import spilab.net.humbleviewimage.view.NextDrawable


class HumbleViewImage : AppCompatImageView {

    var url: String? = null
        set(value) {
            field = value
            if (field != null) {
                downloadIfNeeded()
            }
            cancelNextDrawableTransition()
        }
    var debug = false

    private val downloaderLazy = lazy { HumbleViewDownloader(this) }
    private val downloader: HumbleViewDownloader by downloaderLazy
    private var nextDrawable: NextDrawable? = null
    private var lastKnowSize: HumbleViewSize? = null
    private var currentBitmapId: HumbleBitmapId? = null

    private var bitmapDebug: BitmapDebug? = null
    private val viewDebug by lazy { HumbleViewImageDebug(this.context) }

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
            debug = styledAttributes.getBoolean(R.styleable.HumbleViewImage_debug, false)
        } finally {
            styledAttributes.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lastKnowSize = HumbleViewSize(w, h)
        downloadIfNeeded()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelNextDrawableTransition()
    }

    override fun onDraw(canvas: Canvas?) {
        nextDrawable?.prepareCurrentDrawable(this)
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
        if (debug && canvas != null && bitmapDebug != null) {
            viewDebug.onDraw(canvas, bitmapDebug!!)
        }
    }

    internal fun transitionTo(bitmapId: HumbleBitmapId, humbleViewBitmap: HumbleViewBitmap) {
        if (currentBitmapId == bitmapId) {
            nextDrawable = NextDrawable(this, humbleViewBitmap.bitmap)
            bitmapDebug = humbleViewBitmap.debug
            if (ViewCompat.isAttachedToWindow(this)) {
                invalidate()
            }
        }
    }

    private fun downloadIfNeeded() {
        if (url != null && lastKnowSize != null) {
            val nextBitmapId = HumbleBitmapId(url!!, lastKnowSize!!)
            if (nextBitmapId != currentBitmapId) {
                currentBitmapId = HumbleBitmapId(url!!, lastKnowSize!!)
                downloader.start(context.applicationContext, currentBitmapId!!)
            }
        }
    }

    private inline fun cancelNextDrawableTransition() {
        nextDrawable = null
    }
}