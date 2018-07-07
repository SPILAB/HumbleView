package spilab.net.humbleview.offline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleview.R

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
