package spilab.net.humbleview.scaletype

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import spilab.net.humbleview.R
import spilab.net.humbleview.offline.OfflineActivity

class ScaleTypeActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ScaleTypeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale_type)
    }
}
