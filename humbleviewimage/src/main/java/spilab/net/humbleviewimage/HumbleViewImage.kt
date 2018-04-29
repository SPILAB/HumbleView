package spilab.net.humbleviewimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import java.net.HttpURLConnection
import java.net.URL


class HumbleViewImage : AppCompatImageView {

    private var url: String? = null

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
        } finally {
            styledAttributes.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (url != null) {
            downloadImage(url!!)
        }
    }

    private fun downloadImage(url: String) {
        Thread {
            var bitmap: Bitmap? = null
            val uri = URL(url)
            try {
                val urlConnection = uri.openConnection() as HttpURLConnection
                try {
                    val statusCode = urlConnection.responseCode
                    if (statusCode == 200) {
                        val inputStream = urlConnection.inputStream
                        if (inputStream != null) {
                            bitmap = BitmapFactory.decodeStream(inputStream)
                        }
                    }
                } finally {
                    urlConnection.disconnect()
                }
            } catch (exception: Exception) {
                // For network exception, we do not need to log,
                // we simply cannot retrieve the resource.
            }
            if (bitmap != null) {
                this.post({
                    setImageBitmap(bitmap)
                })
            }
        }.start()
    }
}