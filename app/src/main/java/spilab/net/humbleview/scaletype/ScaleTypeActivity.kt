package spilab.net.humbleview.scaletype

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView.ScaleType
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleview.R

/**
 * Two scales types feature
 *
 * The Activity ScaleTypeActivity shows the possibility to set two different scales types,
 * One for your placeholder:
 * app:srcCompat="@drawable/ic_photo_black_48px"
 * android:scaleType="center"
 *
 * And another one for the loaded image:
 * app:url="https://c1.staticflickr.com/1/501/20080796120_fa1b37a709_h.jpg"
 * app:loadedImageScaleType="centerCrop"
 *
 * By default the loaded image will use the same scale type than the place holder:
 *
 * You can set the loaded image scale type in your layout XML:
 * <spilab.net.humbleimageview.HumbleImageView
 *      ...
 *      app:loadedImageScaleType="centerCrop"
 * Or in your code:
 * findViewById<HumbleImageView>(R.id.scaleLoadedImageFromCode).setLoadedImageScaleType(ScaleType.CENTER_CROP)
 */
class ScaleTypeActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ScaleTypeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale_type)
        findViewById<HumbleImageView>(R.id.scaleLoadedImageFromCode).setLoadedImageScaleType(ScaleType.CENTER_CROP)
    }
}
