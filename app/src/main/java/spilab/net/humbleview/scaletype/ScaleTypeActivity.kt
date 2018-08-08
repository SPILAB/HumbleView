package spilab.net.humbleview.scaletype

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView.ScaleType
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleview.R

/**
 * Loaded scale types feature
 *
 * The Activity ScaleTypeActivity shows the possibility to set different scales types,
 * on for the loaded image and one for app:srcCompat, used as placeholder:
 *
 * <spilab.net.humbleimageview.HumbleImageView
 *      app:srcCompat="@drawable/ic_photo_black_48px"
 *      android:scaleType="center"
 *      app:url="https://c1.staticflickr.com/1/501/20080796120_fa1b37a709_h.jpg"
 *      app:loadedImageScaleType="centerCrop"
 *
 * When the loading and transition of image set throught the url is complete,
 * android:scaleType will be replaced by app:loadedImageScaleType.
 * By default, the loaded image scale equals the image scale.
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
        val humbleImageViews = getHumbleViews()
        setUrl(humbleImageViews)
        setLoadedScaleTypeByCode(humbleImageViews)
        bondWithToast(humbleImageViews)
    }

    private fun bondWithToast(humbleImageViews: MutableList<HumbleImageView>) {
        for (humbleImageView in humbleImageViews) {
            humbleImageView.setOnClickListener {
                Toast.makeText(this, "ScaleType=${humbleImageView.scaleType}", LENGTH_LONG).show()
            }
        }
    }

    /**
     * The humble view loaded scale type are set in the xml for views 0..3,
     * Here we show an example of loaded scale type set by the code for views 4..7
     */
    private fun setLoadedScaleTypeByCode(humbleImageViews: MutableList<HumbleImageView>) {
        humbleImageViews[4].setLoadedImageScaleType(ScaleType.FIT_END)
        humbleImageViews[5].setLoadedImageScaleType(ScaleType.FIT_START)
        humbleImageViews[6].setLoadedImageScaleType(ScaleType.FIT_XY)
        humbleImageViews[7].setLoadedImageScaleType(ScaleType.MATRIX)
    }

    private fun setUrl(humbleImageViews: MutableList<HumbleImageView>) {
        for (humbleImageView in humbleImageViews) {
            humbleImageView.setUrl("https://c1.staticflickr.com/5/4323/35361451933_f752a62c17_b.jpg")
        }
    }

    private fun getHumbleViews(): MutableList<HumbleImageView> {
        val humbleImageView = mutableListOf<HumbleImageView>()
        arrayOf(R.id.humbleImageView0, R.id.humbleImageView1, R.id.humbleImageView2, R.id.humbleImageView3,
                R.id.humbleImageView4, R.id.humbleImageView5, R.id.humbleImageView6, R.id.humbleImageView7)
                .forEachIndexed { index, id -> humbleImageView.add(index, findViewById(id)) }
        return humbleImageView
    }
}
