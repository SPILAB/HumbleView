package spilab.net.humbleimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.support.v4.view.ViewCompat
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.*
import spilab.net.humbleimageview.model.drawable.DrawableResource
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.presenter.HumbleViewPresenter
import spilab.net.humbleimageview.view.HumbleTransition
import spilab.net.humbleimageview.view.HumbleViewImageDebug


class HumbleImageView : AppCompatImageView {

    companion object {
        const val CURRENT_IDX = 0
    }

    private var humbleTransition: HumbleTransition? = null
    private var lastKnowSize = ViewSize()
    private val imageViewDrawables = arrayOf(ImageViewDrawable(this), ImageViewDrawable(this))
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
                R.styleable.HumbleImageView, defStyleAttr, 0)

        try {
            presenter.model.url = styledAttributes.getString(R.styleable.HumbleImageView_url)
            debug = styledAttributes.getBoolean(R.styleable.HumbleImageView_debug, false)
        } finally {
            styledAttributes.recycle()
        }
        synchronizeCurrentImageViewDrawables()
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
        synchronizeCurrentImageViewDrawables()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        synchronizeCurrentImageViewDrawables()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        synchronizeCurrentImageViewDrawables()
    }

    override fun setImageResource(resId: Int) {
        var drawable: Drawable? = null
        if (lastKnowSize.isValid()) {
            drawable = presenter.getRecycledDrawableResource(resId, lastKnowSize, alpha)
        }
        if (drawable == null) {
            drawable = AppCompatResources.getDrawable(context, resId)
            if (drawable != null) {
                drawable = DrawableResource(drawable, resId)
                presenter.recycleDrawable(drawable)
            }
        }
        setImageDrawable(drawable)
        configureFromImageView()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        synchronizeCurrentImageViewDrawables()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val hasFrame = super.setFrame(l, t, r, b)
        configureFromImageView()
        return hasFrame
    }

    override fun onDraw(canvas: Canvas) {
        humbleTransition?.setupAlpha()
        for (index in 0 until imageViewDrawables.size) {
            imageViewDrawables[index].onDraw(canvas)
            presenter.updateDrawableResourceViewSize(imageViewDrawables[index].mDrawable, lastKnowSize)
        }
        drawDebug(canvas)
    }

    internal fun addTransition(drawable: HumbleBitmapDrawable) {
        this.humbleTransition = HumbleTransition(this, imageViewDrawables, drawable)
        if (ViewCompat.isAttachedToWindow(this)) {
            this.humbleTransition?.start()
        } else {
            completeAnimation()
        }
    }

    internal fun completeAnimation() {
        if (humbleTransition != null) {
            val recyclableDrawable = humbleTransition!!.completeAnimationBySwitchingDrawable()
            if (recyclableDrawable != null) {
                presenter.recycleDrawable(recyclableDrawable)
            }
            humbleTransition = null
            synchronizeCurrentImageViewDrawables()
        }
    }

    internal fun isCurrentOrNextDrawableId(bitmapId: HumbleBitmapId): Boolean {
        for (index in 0 until imageViewDrawables.size) {
            val drawable = imageViewDrawables[index].mDrawable
            if (drawable is HumbleBitmapDrawable
                    && drawable.humbleBitmapId == bitmapId) {
                return true
            }
        }
        return false
    }

    /**
     * Must be call each time the drawable is set
     */
    private fun synchronizeCurrentImageViewDrawables() {
        // Warning: imageViewDrawables can be null, because the
        // constructor of ImageView call override methods
        if (imageViewDrawables != null) {
            imageViewDrawables[CURRENT_IDX].mDrawable = this.drawable
        }
    }

    /**
     * Must be call each time the view bounds change
     */
    private fun configureFromImageView() {
        // Warning: imageViewDrawables can be null, because the
        // constructor of ImageView call override methods
        if (imageViewDrawables != null) {
            for (index in 0 until imageViewDrawables.size) {
                imageViewDrawables[index].configureFromImageView()
            }
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