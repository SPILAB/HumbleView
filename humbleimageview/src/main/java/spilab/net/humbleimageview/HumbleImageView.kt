package spilab.net.humbleimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.model.HumbleViewModel
import spilab.net.humbleimageview.model.LoadedImageScaleType
import spilab.net.humbleimageview.model.ViewSize
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.presenter.HumbleViewPresenter
import spilab.net.humbleimageview.view.HumbleTransition
import spilab.net.humbleimageview.view.HumbleViewImageDebug


class HumbleImageView : AppCompatImageView, HumbleTransition.HumbleTransitionListener {

    companion object {
        const val CURRENT_IDX = 0
        const val NEXT_IDX = 1
    }

    private var humbleTransition: HumbleTransition? = null
    private var lastKnowSize = ViewSize()
    private val imageViewDrawables = arrayOf(ImageViewDrawable(this), ImageViewDrawable(this))
    private val viewDebug by lazy {
        HumbleViewImageDebug(this.context)
    }
    private var presenter: HumbleViewPresenter? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        applyCustomAttributes(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyCustomAttributes(context, attrs, defStyleAttr)
    }

    private fun applyCustomAttributes(context: Context, attrs: AttributeSet?,
                                      defStyleAttr: Int) {

        val model = HumbleViewModel(context.applicationContext,
                resources, Handler(Looper.getMainLooper()))
        presenter = HumbleViewPresenter(this, model)


        val styledAttributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.HumbleImageView, defStyleAttr, 0)
        if (styledAttributes != null) {
            try {
                presenter?.setUrl(styledAttributes.getString(R.styleable.HumbleImageView_url))
                presenter?.setOfflineCache(styledAttributes.getBoolean(R.styleable.HumbleImageView_offlineCache, false))
                presenter?.setDebug(styledAttributes.getBoolean(R.styleable.HumbleImageView_debug, false))
                presenter?.initLoadedImageScaleType(styledAttributes)
            } finally {
                styledAttributes.recycle()
            }
        }
        presenter?.synchronizeCurrentImageViewDrawables(this, imageViewDrawables, alpha)
    }

    fun setUrl(url: String) {
        presenter?.setUrl(url)
    }

    fun setOfflineCache(offlineCache: Boolean) {
        presenter?.setOfflineCache(offlineCache)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lastKnowSize = ViewSize(w, h)
        presenter?.setViewSize(lastKnowSize)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.synchronizeCurrentImageViewDrawables(this, imageViewDrawables, alpha)
        presenter?.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        humbleTransition?.completeAnimation()
        presenter?.onDetachedFromWindow(imageViewDrawables)
    }

    override fun setImageIcon(icon: Icon?) {
        super.setImageIcon(icon)
        presenter?.synchronizeCurrentImageViewDrawables(this, imageViewDrawables, alpha)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        presenter?.synchronizeCurrentImageViewDrawables(this, imageViewDrawables, alpha)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        presenter?.synchronizeCurrentImageViewDrawables(this, imageViewDrawables, alpha)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        presenter?.synchronizeCurrentImageViewDrawables(this, imageViewDrawables, alpha)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        presenter?.synchronizeCurrentImageViewDrawables(this, imageViewDrawables, alpha)
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val hasFrame = super.setFrame(l, t, r, b)
        presenter?.configureFromImageView(this, imageViewDrawables)
        return hasFrame
    }

    override fun onDraw(canvas: Canvas) {
        humbleTransition?.startIfNeeded()
        humbleTransition?.setupAlpha()
        for (index in 0 until imageViewDrawables.size) {
            imageViewDrawables[index].onDraw(canvas)
        }
        drawDebug(canvas)
    }

    internal fun addTransition(drawable: HumbleBitmapDrawable,
                               loadedImageScaleType: LoadedImageScaleType) {
        if (ViewCompat.isAttachedToWindow(this)) {
            humbleTransition = HumbleTransition(this, imageViewDrawables, drawable,
                    loadedImageScaleType, this)
        }
    }

    override fun onTransitionCompleted() {
        this.humbleTransition = null
        presenter?.onTransitionCompleted(imageViewDrawables[NEXT_IDX])
    }

    internal fun isCurrentOrNextDrawableId(humbleResourceId: HumbleResourceId): Boolean {
        for (index in 0 until imageViewDrawables.size) {
            val drawable = imageViewDrawables[index].getDrawable()
            if (drawable is HumbleBitmapDrawable
                    && drawable.humbleResourceId == humbleResourceId) {
                return true
            }
        }
        return false
    }

    private inline fun drawDebug(canvas: Canvas?) {
        if ((presenter?.getDebug() == true || HumbleViewAPI.debug) && canvas != null) {
            val currentDrawable = drawable
            var sampleSize = -1
            if (currentDrawable is HumbleBitmapDrawable) {
                sampleSize = currentDrawable.sampleSize
            }
            viewDebug.onDraw(canvas, sampleSize, humbleTransition)
        }
    }

    fun setLoadedImageScaleType(scaleType: ScaleType) {
        presenter?.setLoadedImageScaleType(scaleType)
    }
}