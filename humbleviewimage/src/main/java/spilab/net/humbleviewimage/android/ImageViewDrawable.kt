package spilab.net.humbleviewimage.android

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ImageView.ScaleType

/**
 * This is mainly the code of the original Android ImageView, from API level 27
 */
class ImageViewDrawable(private val imageView: ImageView, drawable: Drawable) {

    val mDrawable: Drawable = drawable

    private var mDrawableWidth: Int
    private var mDrawableHeight: Int

    private var mPaddingLeft: Int
    private var mPaddingRight: Int
    private var mPaddingTop: Int
    private var mPaddingBottom: Int

    private var mScaleType: ImageView.ScaleType
    private var mHaveFrame: Boolean

    private var mMatrix = Matrix()
    private var mDrawMatrix: Matrix? = null

    private var mCropToPadding: Boolean

    private var mScrollX: Int
    private var mScrollY: Int

    private var mRight: Int
    private var mLeft: Int
    private var mBottom: Int
    private var mTop: Int

    private val mTempSrc = RectF()
    private val mTempDst = RectF()

    private val sS2FArray = mapOf(
            Pair(ScaleType.FIT_XY, Matrix.ScaleToFit.FILL),
            Pair(ScaleType.FIT_START, Matrix.ScaleToFit.START),
            Pair(ScaleType.FIT_CENTER, Matrix.ScaleToFit.CENTER),
            Pair(ScaleType.FIT_END, Matrix.ScaleToFit.END))

    init {
        mDrawableWidth = this.mDrawable.intrinsicWidth
        mDrawableHeight = this.mDrawable.intrinsicHeight
        mHaveFrame = this.imageView.width > 0 || this.imageView.height > 0
        mPaddingLeft = this.imageView.paddingLeft
        mPaddingRight = this.imageView.paddingRight
        mPaddingTop = this.imageView.paddingTop
        mPaddingBottom = this.imageView.paddingBottom
        mScaleType = this.imageView.scaleType
        mMatrix.set(this.imageView.matrix)
        mCropToPadding = this.imageView.cropToPadding
        mScrollX = this.imageView.scrollX
        mScrollY = this.imageView.scrollY
        mRight = this.imageView.right
        mLeft = this.imageView.left
        mBottom = this.imageView.bottom
        mTop = this.imageView.top
        configureBounds()
    }

    fun onDraw(canvas: Canvas) {
        if (mDrawableWidth == 0 || mDrawableHeight == 0) {
            return      // nothing to draw (empty bounds)
        }

        if (mDrawMatrix == null && mPaddingTop === 0 && mPaddingLeft === 0) {
            mDrawable.draw(canvas)
        } else {
            val saveCount = canvas.saveCount
            canvas.save()

            if (mCropToPadding) {
                val scrollX = mScrollX
                val scrollY = mScrollY
                canvas.clipRect(scrollX + mPaddingLeft, scrollY + mPaddingTop,
                        scrollX + mRight - mLeft - mPaddingRight,
                        scrollY + mBottom - mTop - mPaddingBottom)
            }

            canvas.translate(mPaddingLeft.toFloat(), mPaddingTop.toFloat())

            if (mDrawMatrix != null) {
                canvas.concat(mDrawMatrix)
            }
            mDrawable.draw(canvas)
            canvas.restoreToCount(saveCount)
        }
    }

    private fun configureBounds() {
        if (mDrawable == null || !mHaveFrame) {
            return
        }

        val dwidth = mDrawableWidth
        val dheight = mDrawableHeight

        val vwidth = imageView.width - mPaddingLeft - mPaddingRight
        val vheight = imageView.height - mPaddingTop - mPaddingBottom

        val fits = (dwidth < 0 || vwidth == dwidth) && (dheight < 0 || vheight == dheight)

        if (dwidth <= 0 || dheight <= 0 || ScaleType.FIT_XY == mScaleType) {
            /* If the drawable has no intrinsic size, or we're told to
                scaletofit, then we just fill our entire view.
            */
            mDrawable.setBounds(0, 0, vwidth, vheight)
            mDrawMatrix = null
        } else {
            // We need to do the scaling ourself, so have the drawable
            // use its native size.
            mDrawable.setBounds(0, 0, dwidth, dheight)

            if (ScaleType.MATRIX == mScaleType) {
                // Use the specified matrix as-is.
                if (mMatrix.isIdentity) {
                    mDrawMatrix = null
                } else {
                    mDrawMatrix = mMatrix
                }
            } else if (fits) {
                // The bitmap fits exactly, no transform needed.
                mDrawMatrix = null
            } else if (ScaleType.CENTER == mScaleType) {
                // Center bitmap in view, no scaling.
                mDrawMatrix = mMatrix
                mDrawMatrix?.setTranslate(Math.round((vwidth - dwidth) * 0.5f).toFloat(),
                        Math.round((vheight - dheight) * 0.5f).toFloat())
            } else if (ScaleType.CENTER_CROP == mScaleType) {
                mDrawMatrix = mMatrix

                val scale: Float
                var dx = 0f
                var dy = 0f

                if (dwidth * vheight > vwidth * dheight) {
                    scale = vheight.toFloat() / dheight.toFloat()
                    dx = (vwidth - dwidth * scale) * 0.5f
                } else {
                    scale = vwidth.toFloat() / dwidth.toFloat()
                    dy = (vheight - dheight * scale) * 0.5f
                }

                mDrawMatrix?.setScale(scale, scale)
                mDrawMatrix?.postTranslate(Math.round(dx).toFloat(), Math.round(dy).toFloat())
            } else if (ScaleType.CENTER_INSIDE == mScaleType) {
                mDrawMatrix = mMatrix
                val scale: Float
                val dx: Float
                val dy: Float

                if (dwidth <= vwidth && dheight <= vheight) {
                    scale = 1.0f
                } else {
                    scale = Math.min(vwidth.toFloat() / dwidth.toFloat(),
                            vheight.toFloat() / dheight.toFloat())
                }

                dx = Math.round((vwidth - dwidth * scale) * 0.5f).toFloat()
                dy = Math.round((vheight - dheight * scale) * 0.5f).toFloat()

                mDrawMatrix?.setScale(scale, scale)
                mDrawMatrix?.postTranslate(dx, dy)
            } else {
                // Generate the required transform.
                mTempSrc.set(0f, 0f, dwidth.toFloat(), dheight.toFloat())
                mTempDst.set(0f, 0f, vwidth.toFloat(), vheight.toFloat())

                mDrawMatrix = Matrix(mMatrix)
                mDrawMatrix?.setRectToRect(mTempSrc, mTempDst, scaleTypeToScaleToFit(mScaleType))
            }
        }
    }

    private fun scaleTypeToScaleToFit(st: ScaleType): Matrix.ScaleToFit {
        // ScaleToFit enum to their corresponding Matrix.ScaleToFit values
        return sS2FArray[st]!!
    }
}