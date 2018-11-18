package spilab.net.humbleview.transform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val viewManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.TransformListView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = TransformListAdapter(IMAGES_URLS)
        }
    }
}
