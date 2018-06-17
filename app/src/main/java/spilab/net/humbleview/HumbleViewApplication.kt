package spilab.net.humbleview

import android.app.Application
import spilab.net.humbleimageview.model.HumbleViewAPI

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
        HumbleViewAPI.log = true
        // HumbleViewAPI.debug = true
    }
}
