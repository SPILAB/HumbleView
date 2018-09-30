package spilab.net.humbleview.transform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import spilab.net.humbleview.R
import spilab.net.humbleview.data.ImagesUrls.Companion.IMAGES_URLS

class TransformActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TransformActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transform)
        val viewManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.TransformListView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = TransformListAdapter(IMAGES_URLS)
        }
    }
}
