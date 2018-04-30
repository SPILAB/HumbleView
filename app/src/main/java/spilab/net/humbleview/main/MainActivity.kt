package spilab.net.humbleview.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import spilab.net.humbleview.R
import spilab.net.humbleview.singleimage.SingleImageActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickSingleImage(v: View) {
        startActivity(SingleImageActivity.createIntent(this))
    }
}
