package spilab.net.humbleimageview

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import spilab.net.humbleimageview.android.ImageViewDrawable
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleimageview.features.HumbleImageFeatures
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.features.transition.drawable.DrawableImageViewDelegate
import spilab.net.humbleimageview.features.transition.drawable.DrawableSecondaryDelegate
import spilab.net.humbleimageview.features.transition.scale.ScaleImageViewDelegate
import spilab.net.humbleimageview.features.transition.scale.ScaleSecondaryDelegate
import spilab.net.humbleimageview.model.HumbleResourceId
import spilab.net.humbleimageview.model.HumbleViewModel
import spilab.net.humbleimageview.model.ViewSize
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.view.HumbleViewImageDebug


class HumbleImageView : AppCompatImageView {

    private val secondaryScaleType = ScaleSecondaryDelegate(this)

    internal val imageViewDrawables = arrayOf(
            ImageViewDrawable(this, DrawableImageViewDelegate(this), ScaleImageViewDelegate(this)),
            ImageViewDrawable(this, DrawableSecondaryDelegate(), secondaryScaleType)
    )

    private var lastKnowSize = ViewSize()
    private val viewDebug by lazy {
        HumbleViewImageDebug(this.context)
    }
    private var features: HumbleImageFeatures? = null

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
        features = HumbleImageFeatures(this, model)


        val styledAttributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.HumbleImageView, defStyleAttr, 0)
        if (styledAttributes != null) {
            try {
                features?.setUrl(styledAttributes.getString(R.styleable.HumbleImageView_url))
                features?.setOfflineCache(styledAttributes.getBoolean(R.styleable.HumbleImageView_offlineCache, false))
                features?.setDebug(styledAttributes.getBoolean(R.styleable.HumbleImageView_debug, false))
                secondaryScaleType.initScaleType(styledAttributes)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    fun setUrl(url: String) {
        features?.setUrl(url)
    }

    fun setOfflineCache(offlineCache: Boolean) {
        features?.setOfflineCache(offlineCache)
    }

    fun setLoadedImageScaleType(scaleType: ScaleType) {
        secondaryScaleType.setScaleType(scaleType)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lastKnowSize = ViewSize(w, h)
        features?.setViewSize(lastKnowSize)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        features?.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        features?.onDetachedFromWindow()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val hasFrame = super.setFrame(l, t, r, b)
        features?.configureFromImageView()
        return hasFrame
    }

    override fun setImageResource(resId: Int) {
        var drawableFromResId = features?.getVectorDrawable(resId, width, height)
        if (drawableFromResId == null) {
            super.setImageResource(resId)
            drawableFromResId = VectorDrawableFromResId(drawable, resId, width, height)
        }
        setImageDrawable(drawableFromResId)
    }

    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) features?.onResume() else features?.onPause()
    }

    override fun onDraw(canvas: Canvas) {
        features?.prepareOnDraw()
        for (index in 0 until imageViewDrawables.size) {
            imageViewDrawables[index].onDraw(canvas)
        }
        drawDebug(canvas)
    }

    internal fun isCurrentOrNextDrawableId(humbleResourceId: HumbleResourceId): Boolean {
        return humbleResourceId.isPresentIn(imageViewDrawables)
    }

    private fun drawDebug(canvas: Canvas?) {
        if ((features?.getDebug() == true || HumbleViewAPI.debug) && canvas != null) {
            for (index in 0 until imageViewDrawables.size) {
                val drawable = imageViewDrawables[index].getDrawable()
                if (drawable is HumbleBitmapDrawable) {
                    viewDebug.onDraw(canvas, drawable)
                    return
                }
            }
        }
    }
}