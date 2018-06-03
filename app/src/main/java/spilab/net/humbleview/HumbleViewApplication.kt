package spilab.net.humbleview

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import spilab.net.humbleimageview.model.HumbleViewConfig

class HumbleViewApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO: UNCOMMENT TO CHECK MEMORY LEAK
        /*
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        */

        // TODO: TEMPORARY POC
        HumbleViewConfig.installHTTPCache(applicationContext)
        HumbleViewConfig.log = true
        // HumbleViewConfig.debug = true
    }
}
