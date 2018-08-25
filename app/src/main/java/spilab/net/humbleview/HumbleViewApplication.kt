package spilab.net.humbleview

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import spilab.net.humbleimageview.api.HumbleViewAPI
import spilab.net.humbleview.settings.SettingsActivity


class HumbleViewApplication : Application() {

    companion object {
        fun onSettingsUpdate(context: Context, key: String) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            when (key) {
                SettingsActivity.PREF_CACHE_SIZE_LIST -> {
                    HumbleViewAPI.http.setHttpCacheSize(sharedPref.getString(SettingsActivity.PREF_CACHE_SIZE_LIST,
                            HumbleViewAPI.http.toString()).toLong())
                }
                SettingsActivity.PREF_FADING_SPEED_LIST -> {
                    HumbleViewAPI.fadingSpeedMillis = sharedPref.getString(SettingsActivity.PREF_FADING_SPEED_LIST,
                            HumbleViewAPI.DEFAULT_FADING_SPEED_MILLIS.toString()).toLong()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        // TODO: UNCOMMENT TO CHECK MEMORY LEAK
        /*
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        */

        onSettingsUpdate(this.applicationContext, SettingsActivity.PREF_FADING_SPEED_LIST)
        // TODO: TEMPORARY POC
        HumbleViewAPI.log = true
    }
}
