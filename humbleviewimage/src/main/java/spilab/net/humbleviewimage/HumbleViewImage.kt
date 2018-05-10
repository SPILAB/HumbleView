package spilab.net.humbleviewimage

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import spilab.net.humbleviewimage.model.HumbleBitmapDrawable
import spilab.net.humbleviewimage.model.ViewSize
import spilab.net.humbleviewimage.presenter.HumbleViewPresenter
import spilab.net.humbleviewimage.view.HumbleTransitionDrawable
import spilab.net.humbleviewimage.view.HumbleViewImageDebug


class HumbleViewImage : AppCompatImageView {

    internal var humbleTransitionDrawable: HumbleTransitionDrawable? = null
    private var lastKnowSize: ViewSize? = null
    private val viewDebug by lazy { HumbleViewImageDebug(this.context) }
    private var presenter = HumbleViewPresenter(this)
    private var debug = false

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
            presenter.model.url = styledAttributes.getString(R.styleable.HumbleViewImage_url)
            debug = styledAttributes.getBoolean(R.styleable.HumbleViewImage_debug, false)
        } finally {
            styledAttributes.recycle()
        }
    }

    fun setUrl(url: String) {
        presenter.model.url = url
    }

    fun getUrl(): String? {
        return presenter.model.url
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lastKnowSize = ViewSize(w, h)
        presenter.model.viewSize = lastKnowSize
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.stop()
        cancelNextDrawableTransition()
    }

    override fun onDraw(canvas: Canvas?) {
        humbleTransitionDrawable?.prepareCurrentDrawable(this)
        super.onDraw(canvas)
        if (humbleTransitionDrawable != null && canvas != null) {
            humbleTransitionDrawable!!.prepareNextDrawable(this)
            super.onDraw(canvas)
            if (humbleTransitionDrawable!!.isAnimationCompleted()) {
                humbleTransitionDrawable = null
            } else {
                humbleTransitionDrawable!!.restoreCurrentDrawable(this)
            }
        }
        if (debug && canvas != null) {
            val currentDrawable = drawable
            if (currentDrawable is HumbleBitmapDrawable) {
                viewDebug.onDraw(canvas, currentDrawable.sampleSize)
            }
        }
    }

    private inline fun cancelNextDrawableTransition() {
        humbleTransitionDrawable?.completeAnimationImmediately(this)
        humbleTransitionDrawable = null
    }
}