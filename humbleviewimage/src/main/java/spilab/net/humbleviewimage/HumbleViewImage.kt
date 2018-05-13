package spilab.net.humbleviewimage

import android.content.Context
import android.graphics.Canvas
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import spilab.net.humbleviewimage.model.HumbleBitmapDrawable
import spilab.net.humbleviewimage.model.HumbleViewConfig
import spilab.net.humbleviewimage.model.ViewSize
import spilab.net.humbleviewimage.presenter.HumbleViewPresenter
import spilab.net.humbleviewimage.view.HumbleTransition
import spilab.net.humbleviewimage.view.HumbleViewImageDebug


class HumbleViewImage : AppCompatImageView {

    internal var humbleTransition: HumbleTransition? = null
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
    }

    override fun onDraw(canvas: Canvas?) {
        humbleTransition?.prepareFading()
        super.onDraw(canvas)
        if (humbleTransition != null) {
            humbleTransition!!.onDraw(canvas)
        }
        drawDebug(canvas)
    }

    private inline fun drawDebug(canvas: Canvas?) {
        if ((debug || HumbleViewConfig.debug) && canvas != null) {
            val currentDrawable = drawable
            var sampleSize = -1
            if (currentDrawable is HumbleBitmapDrawable) {
                sampleSize = currentDrawable.sampleSize
            }
            viewDebug.onDraw(canvas, sampleSize, humbleTransition)
        }
    }

    internal fun addTransition(humbleTransition: HumbleTransition) {
        this.humbleTransition = humbleTransition
        if (ViewCompat.isAttachedToWindow(this)) {
            this.humbleTransition?.start()
        } else {
            completeAnimation()
        }
    }

    internal fun completeAnimation() {
        humbleTransition?.completeAnimationBySwitchingDrawable()
        humbleTransition = null
    }
}