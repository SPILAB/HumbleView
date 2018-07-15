package spilab.net.humbleview.offline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleview.R

/**
 * Offline mode feature
 * The Activity OfflineActivity shows an example of the offline mode feature of Humble image view.
 * The two images visible in this example use the offline cache,
 * which ensures that these images will be always be available in the cache, regardless of
 * Cache-Control or ETag value.
 * Simply add app:offlineCache="true" in your layout XML to made the image available offline:
 * Or enable it in your code with setOfflineCache
 */
class OfflineActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, OfflineActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline)
        findViewById<HumbleImageView>(R.id.offlineImageFromCode).setOfflineCache(true)
    }
}
