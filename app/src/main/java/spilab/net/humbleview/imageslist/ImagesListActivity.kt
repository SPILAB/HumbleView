package spilab.net.humbleview.imageslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import spilab.net.humbleview.R

class ImagesListActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ImagesListActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_list)

        var viewManager = LinearLayoutManager(this)

        findViewById<RecyclerView>(R.id.ImagesListView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = ImagesListAdapter(arrayOf(
                    "https://c1.staticflickr.com/3/2865/9677214147_e011d8562e_h.jpg",
                    "https://c1.staticflickr.com/1/463/19757310168_8c7300f9ec_h.jpg",
                    "https://c1.staticflickr.com/1/370/19853422393_68049a6a16_h.jpg",
                    "https://c1.staticflickr.com/1/421/19223572274_d036a006e4_h.jpg",
                    "https://c1.staticflickr.com/1/541/19571492784_25f8f4241a_h.jpg",
                    "https://c1.staticflickr.com/5/4203/34480445003_aca00787ba_h.jpg",
                    "https://c1.staticflickr.com/5/4415/36924982015_3dd4e684f3_h.jpg",
                    "https://c1.staticflickr.com/5/4470/37460137921_71a0034bb1_h.jpg",
                    "https://c1.staticflickr.com/1/508/19274589884_16335577d5_h.jpg",
                    "https://c2.staticflickr.com/4/3781/19855879616_5a7921ce63_h.jpg",
                    "https://c1.staticflickr.com/1/773/22820598993_499b77cc46_h.jpg",
                    "https://c1.staticflickr.com/1/473/19276687704_9ad3669864_h.jpg",
                    "https://c2.staticflickr.com/6/5752/22455780319_5710351200_h.jpg",
                    "https://c2.staticflickr.com/6/5708/22429572317_d5dbf2cdda_h.jpg",
                    "https://c1.staticflickr.com/9/8847/17368472122_96235a295a_h.jpg",
                    "https://c1.staticflickr.com/9/8403/28757411074_9f83ab898d_h.jpg",
                    "https://c2.staticflickr.com/6/5574/31383030170_4ba7498812_h.jpg",
                    "https://c1.staticflickr.com/1/449/19666041189_1728a3d10f_h.jpg",
                    "https://c1.staticflickr.com/5/4307/35757415760_7401cd9e86_h.jpg",
                    "https://c1.staticflickr.com/9/8015/29334731836_d24601058a_h.jpg"))
        }
    }
}
