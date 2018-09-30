package spilab.net.humbleview.imageslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import spilab.net.humbleview.R
import spilab.net.humbleview.data.ImagesUrls.Companion.IMAGES_URLS

class ImagesListActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ImagesListActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_list)
        val viewManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.ImagesListView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = ImagesListAdapter(IMAGES_URLS)
        }
    }
}
