package spilab.net.humbleview

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import spilab.net.humbleviewimage.model.HumbleViewConfig

class HumbleViewApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        // TODO: TEMPORARY POC
        HumbleViewConfig.installHTTPCache(applicationContext)
        HumbleViewConfig.debug = true
    }
}
