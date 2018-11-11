package spilab.net.humbleview.slideshow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleview.R

class SlideShowActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, SlideShowActivity::class.java)
        }
    }

    private val slideShowUrls = mapOf(
            R.id.humbleImageViewSmall to arrayOf(
                    "https://c1.staticflickr.com/1/893/41629248124_8e5cbf2bda_z.jpg",
                    "https://c2.staticflickr.com/2/1760/40544646000_18e3c510e2_z.jpg",
                    "https://c2.staticflickr.com/2/1747/41629248734_4c3246ecf6_z.jpg"),
            R.id.humbleImageViewMedium to arrayOf(
                    "https://c1.staticflickr.com/9/8466/8076329318_7f723a3e82_c.jpg",
                    "https://c1.staticflickr.com/9/8336/8076287796_423f271bea_c.jpg",
                    "https://c1.staticflickr.com/9/8336/8076464389_d97895b23a_b.jpg"),
            R.id.humbleImageViewBig to arrayOf(
                    "https://c1.staticflickr.com/1/424/19274113784_6af2ea51c2_h.jpg",
                    "https://c1.staticflickr.com/9/8581/16037256398_fe35810029_h.jpg",
                    "https://c2.staticflickr.com/8/7335/9568816961_a20117b254_h.jpg")
    )

    private lateinit var humbleViews: Map<Int, HumbleImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_show)
        humbleViews = listOf(R.id.humbleImageViewSmall, R.id.humbleImageViewMedium, R.id.humbleImageViewBig).associate { it ->
            Pair<Int, HumbleImageView>(it, findViewById(it))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_slide_show_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.image -> setImage()
            R.id.slideshow -> showSlideShow()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setImage() {
        val url = "https://c1.staticflickr.com/9/8236/8540808390_5a6c044343_h.jpg"
        listOf(R.id.humbleImageViewSmall, R.id.humbleImageViewMedium, R.id.humbleImageViewBig).forEach {
            humbleViews[it]?.setUrl(url)
        }
    }

    private fun showSlideShow() {
        slideShowUrls.forEach {
            humbleViews[it.key]?.setSlideshowUrls(it.value)
        }
    }
}
