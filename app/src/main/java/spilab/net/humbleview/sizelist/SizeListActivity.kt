package spilab.net.humbleview.sizelist

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import spilab.net.humbleimageview.HumbleImageView
import spilab.net.humbleimageview.features.sizelist.SizeList
import spilab.net.humbleimageview.features.sizelist.UrlWithSize
import spilab.net.humbleimageview.view.DebugViewFlags
import spilab.net.humbleimageview.view.ViewSize
import spilab.net.humbleview.R

class SizeListActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, SizeListActivity::class.java)
        }
    }

    private var listA = SizeList(listOf(
            UrlWithSize("https://c2.staticflickr.com/4/3792/20479564021_bcaf53fb7c_n.jpg", ViewSize(320, 213)),
            UrlWithSize("https://c2.staticflickr.com/4/3792/20479564021_bcaf53fb7c_z.jpg", ViewSize(640, 426)),
            UrlWithSize("https://c2.staticflickr.com/4/3792/20479564021_0f4bdc0358_h.jpg", ViewSize(1600, 1066))))
    private var listB = SizeList(listOf(
            UrlWithSize("https://c2.staticflickr.com/8/7337/16171849087_f1abed0cae_n.jpg", ViewSize(320, 213)),
            UrlWithSize("https://c2.staticflickr.com/8/7337/16171849087_f1abed0cae_z.jpg", ViewSize(640, 427)),
            UrlWithSize("https://c2.staticflickr.com/8/7337/16171849087_ed2022bd9f_h.jpg", ViewSize(1600, 1067))))
    private var listC = SizeList(listOf(
            UrlWithSize("https://c1.staticflickr.com/1/665/20850594540_7d7da5c519_n.jpg", ViewSize(320, 213)),
            UrlWithSize("https://c1.staticflickr.com/1/665/20850594540_7d7da5c519_z.jpg", ViewSize(640, 427)),
            UrlWithSize("https://c1.staticflickr.com/1/665/20850594540_2ecec1b525_h.jpg", ViewSize(1600, 1067))))
    private var listD = SizeList(listOf(
            UrlWithSize("https://c2.staticflickr.com/8/7306/9359792578_9ca5949095_n.jpg", ViewSize(320, 180)),
            UrlWithSize("https://c2.staticflickr.com/8/7306/9359792578_9ca5949095_z.jpg", ViewSize(640, 359)),
            UrlWithSize("https://c2.staticflickr.com/8/7306/9359792578_1514472f36_h.jpg", ViewSize(1600, 899))))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_list)
        for (entry in mapOf(R.id.humbleImageViewBig to listA, R.id.humbleImageViewMediumA to listB,
                R.id.humbleImageViewMediumB to listC, R.id.humbleImageViewMediumC to listD,
                R.id.humbleImageViewSmallA to listA, R.id.humbleImageViewSmallB to listB,
                R.id.humbleImageViewSmallC to listC, R.id.humbleImageViewSmallD to listD)) {
            findViewById<HumbleImageView>(entry.key)
                    .setUrls(entry.value)
                    .setDebugFlags(DebugViewFlags.REQUEST_VIEW_SIZE.value)
        }
    }
}
