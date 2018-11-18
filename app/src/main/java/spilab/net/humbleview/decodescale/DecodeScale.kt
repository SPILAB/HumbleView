package spilab.net.humbleview.decodescale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import spilab.net.humbleview.R

class DecodeScale : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, DecodeScale::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decode_scale)
    }
}
