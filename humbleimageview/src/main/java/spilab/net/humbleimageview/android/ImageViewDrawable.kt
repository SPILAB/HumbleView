package spilab.net.humbleimageview.android

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ImageView.ScaleType

/**
 * The code is mainly a copy/past of the original Android ImageView,
 * from API level 27:
 * The matrix assign was was replaced with a set.
 */
internal class ImageViewDrawable(private val imageView: ImageView) {

    var mDrawable: Drawable? = null

    private var mDrawableWidth: Int = 0
    private var mDrawableHeight: Int = 0

    private var mPaddingLeft: Int = 0
    private var mPaddingRight: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingBottom: Int = 0

    private var mScaleType: ImageView.ScaleType = ScaleType.FIT_CENTER
    private var mHaveFrame: Boolean = false

    private var mMatrix = Matrix()
    private var mDrawMatrix: Matrix? = null

    private var mCropToPadding: Boolean = false

    private var mScrollX: Int = 0
    private var mScrollY: Int = 0

    private var mRight: Int = 0
    private var mLeft: Int = 0
    private var mBottom: Int = 0
    private var mTop: Int = 0

    private val mTempSrc = RectF()
    private val mTempDst = RectF()

    private val sS2FArray = mapOf(
            Pair(ScaleType.FIT_XY, Matrix.ScaleToFit.FILL),
            Pair(ScaleType.FIT_START, Matrix.ScaleToFit.START),
            Pair(ScaleType.FIT_CENTER, Matrix.ScaleToFit.CENTER),
            Pair(ScaleType.FIT_END, Matrix.ScaleToFit.END))

    fun copyImageView() {
        mDrawableWidth = mDrawable?.intrinsicWidth ?: 0
        mDrawableHeight = mDrawable?.intrinsicHeight ?: 0
        mHaveFrame = imageView.width > 0 || imageView.height > 0
        mPaddingLeft = imageView.paddingLeft
        mPaddingRight = imageView.paddingRight
        mPaddingTop = imageView.paddingTop
        mPaddingBottom = imageView.paddingBottom
        mScaleType = imageView.scaleType
        mMatrix.set(imageView.matrix)
        mCropToPadding = imageView.cropToPadding
        mScrollX = imageView.scrollX
        mScrollY = imageView.scrollY
        mRight = imageView.right
        mLeft = imageView.left
        mBottom = imageView.bottom
        mTop = imageView.top
    }

    fun configureBounds() {
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
            mDrawable?.setBounds(0, 0, vwidth, vheight)
            mDrawMatrix = null
        } else {
            // We need to do the scaling ourself, so have the drawable
            // use its native size.
            mDrawable?.setBounds(0, 0, dwidth, dheight)

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

    fun onDraw(canvas: Canvas) {
        if (mDrawableWidth == 0 || mDrawableHeight == 0) {
            return      // nothing to draw (empty bounds)
        }

        if (mDrawMatrix == null && mPaddingTop === 0 && mPaddingLeft === 0) {
            mDrawable?.draw(canvas)
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
            mDrawable?.draw(canvas)
            canvas.restoreToCount(saveCount)
        }
    }

    private fun scaleTypeToScaleToFit(st: ScaleType): Matrix.ScaleToFit {
        // ScaleToFit enum to their corresponding Matrix.ScaleToFit values
        return sS2FArray[st]!!
    }
}