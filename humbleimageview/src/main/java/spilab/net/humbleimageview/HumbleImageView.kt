package spilab.net.humbleimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import spilab.net.humbleimageview.android.AndroidImageViewDrawable
import spilab.net.humbleimageview.drawable.HumbleBitmapDrawable
import spilab.net.humbleimageview.features.HumbleImageFeatures
import spilab.net.humbleimageview.features.memory.VectorDrawableFromResId
import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.features.request.ResourceId
import spilab.net.humbleimageview.features.sizelist.SizeList
import spilab.net.humbleimageview.features.transition.drawable.DrawableImageViewDelegate
import spilab.net.humbleimageview.features.transition.drawable.DrawableSecondaryDelegate
import spilab.net.humbleimageview.features.transition.scale.ScaleDelegate
import spilab.net.humbleimageview.view.DebugView
import spilab.net.humbleimageview.view.ViewSize


class HumbleImageView : AppCompatImageView {

    private val scaleDelegate = ScaleDelegate(this)

    internal val imageViewDrawables = arrayOf(
            AndroidImageViewDrawable(this, DrawableImageViewDelegate(this), scaleDelegate /* ScaleImageViewDelegate(this)*/),
            AndroidImageViewDrawable(this, DrawableSecondaryDelegate(), scaleDelegate)
    )

    private var lastKnowSize = ViewSize()
    private val viewDebug by lazy {
        DebugView(this.context)
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

        val model = HumbleViewRequest(context.applicationContext,
                resources, Handler(Looper.getMainLooper()))
        features = HumbleImageFeatures(this, model)


        val styledAttributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.HumbleImageView, defStyleAttr, 0)
        if (styledAttributes != null) {
            try {
                features?.setUrl(styledAttributes.getString(R.styleable.HumbleImageView_url))
                features?.setOfflineCache(styledAttributes.getBoolean(R.styleable.HumbleImageView_offlineCache, false))
                val debugFlags = styledAttributes.getString(R.styleable.HumbleImageView_debugFlags)
                if (debugFlags != null) {
                    viewDebug.setDebugFlags(debugFlags)
                }
                scaleDelegate.initLoadedScaleType(styledAttributes)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    fun setUrl(url: String): HumbleImageView {
        features?.setUrl(url)
        return this
    }

    fun setUrls(urls: SizeList): HumbleImageView {
        features?.setUrls(urls)
        return this
    }

    fun setOfflineCache(offlineCache: Boolean): HumbleImageView {
        features?.setOfflineCache(offlineCache)
        return this
    }

    fun setLoadedImageScaleType(scaleType: ScaleType): HumbleImageView {
        scaleDelegate.setLoadedScaleType(scaleType)
        return this
    }

    fun setDebugFlags(debugFlags: Int): HumbleImageView {
        viewDebug.setDebugFlags(debugFlags)
        return this
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

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        features?.drawableReplaced()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        features?.drawableReplaced()
    }

    override fun setImageIcon(icon: Icon?) {
        super.setImageIcon(icon)
        features?.drawableReplaced()
    }

    override fun setImageResource(resId: Int) {
        features?.drawableReplaced()
        var drawableFromResId = features?.getVectorDrawable(resId, width, height)
        if (drawableFromResId == null) {
            super.setImageResource(resId)
            drawableFromResId = VectorDrawableFromResId(drawable, resId, width, height)
        }
        super.setImageDrawable(drawableFromResId)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        features?.drawableReplaced()

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

    internal fun isCurrentOrNextDrawableId(resourceId: ResourceId): Boolean {
        return resourceId.isPresentIn(imageViewDrawables)
    }

    private fun drawDebug(canvas: Canvas?) {
        if (canvas != null) {
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