package spilab.net.humbleviewimage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import spilab.net.humbleviewimage.android.ImageViewDrawable
import spilab.net.humbleviewimage.model.HumbleBitmapDrawable
import spilab.net.humbleviewimage.model.HumbleBitmapId
import spilab.net.humbleviewimage.model.HumbleViewConfig
import spilab.net.humbleviewimage.model.ViewSize
import spilab.net.humbleviewimage.presenter.HumbleViewPresenter
import spilab.net.humbleviewimage.view.HumbleTransition
import spilab.net.humbleviewimage.view.HumbleViewImageDebug


class HumbleViewImage : AppCompatImageView {

    private var humbleTransition: HumbleTransition? = null
    private var lastKnowSize: ViewSize? = null
    private val imageViewDrawables = arrayOf(
            ImageViewDrawable(this),
            ImageViewDrawable(this))
    private val viewDebug by lazy {
        HumbleViewImageDebug(this.context)
    }
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
        updateImageViewDrawables()
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

    override fun setImageIcon(icon: Icon?) {
        super.setImageIcon(icon)
        updateImageViewDrawables()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        updateImageViewDrawables()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updateImageViewDrawables()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        updateImageViewDrawables()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        updateImageViewDrawables()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val hasFrame = super.setFrame(l, t, r, b)
        updateImageViewDrawables()
        return hasFrame
    }

    override fun onDraw(canvas: Canvas) {
        humbleTransition?.setupAlpha()
        imageViewDrawables[0].onDraw(canvas)
        imageViewDrawables[1].onDraw(canvas)
        drawDebug(canvas)
    }

    internal fun addTransition(drawable: HumbleBitmapDrawable) {
        this.humbleTransition = HumbleTransition(this, imageViewDrawables, drawable)
        updateImageViewDrawables()
        if (ViewCompat.isAttachedToWindow(this)) {
            this.humbleTransition?.start()
        } else {
            completeAnimation()
        }
    }

    internal fun completeAnimation() {
        humbleTransition?.completeAnimationBySwitchingDrawable()
        humbleTransition = null
        updateImageViewDrawables()
    }

    internal fun isCurrentOrNextDrawableId(bitmapId: HumbleBitmapId): Boolean {
        imageViewDrawables.forEach {
            if (it.mDrawable is HumbleBitmapDrawable) {
                if ((it.mDrawable as HumbleBitmapDrawable).humbleBitmapId == bitmapId) return true
            }
        }
        return false
    }

    /**
     * Warning: imageViewDrawables can be null, because the
     * constructor of ImageView call override methods
     */
    private fun updateImageViewDrawables() {
        imageViewDrawables?.forEachIndexed { index, imageViewDrawable ->
            if (index == 0) {
                imageViewDrawable.mDrawable = this.drawable
            }
            imageViewDrawable.copyImageView()
            imageViewDrawable.configureBounds()
        }
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
}