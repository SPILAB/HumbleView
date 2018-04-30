package spilab.net.humbleview.singleimage

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import spilab.net.humbleview.R

class SingleImageActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            val intent = Intent(context, SingleImageActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_image)
    }
}
