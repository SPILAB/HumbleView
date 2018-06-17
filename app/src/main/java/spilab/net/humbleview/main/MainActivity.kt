package spilab.net.humbleview.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import spilab.net.humbleview.R
import spilab.net.humbleview.decodescale.DecodeScale
import spilab.net.humbleview.imageslist.ImagesListActivity
import spilab.net.humbleview.settings.SettingsActivity
import spilab.net.humbleview.singleimage.SingleImageActivity


class MainActivity : AppCompatActivity(), MainSamplesAdapter.MainSamplesAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.samplesListView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = MainSamplesAdapter(arrayOf(
                    MainSamplesAdapter.SampleButton(R.string.single_image,
                            "https://c1.staticflickr.com/3/2865/9677214147_e011d8562e_h.jpg",
                            SingleImageActivity.createIntent(this@MainActivity)),
                    MainSamplesAdapter.SampleButton(R.string.images_list,
                            "https://c2.staticflickr.com/8/7306/12631226523_d2926814c7_b.jpg",
                            ImagesListActivity.createIntent(this@MainActivity)),
                    MainSamplesAdapter.SampleButton(R.string.decode_scale,
                            "https://c1.staticflickr.com/5/4227/34581375010_6d4e176472_h.jpg",
                            DecodeScale.createIntent(this@MainActivity)),
                    MainSamplesAdapter.SampleButton(R.string.settings,
                            "https://c1.staticflickr.com/1/653/22711070429_7982d6131e_h.jpg",
                            SettingsActivity.createIntent(this@MainActivity))
            ),
                    this@MainActivity)
        }
    }

    override fun onClick(intent: Intent) {
        startActivity(intent)
    }
}
