package spilab.net.humbleview

import android.app.Application
import android.content.Context
import spilab.net.humbleimageview.model.HumbleViewAPI
import spilab.net.humbleview.settings.SettingsActivity
import android.preference.PreferenceManager


class HumbleViewApplication : Application() {

    companion object {
        fun onSettingsUpdate(context: Context, key: String) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            when (key) {
                SettingsActivity.PREF_CACHE_SIZE_LIST -> {
                    HumbleViewAPI.setCacheSize(sharedPref.getString(SettingsActivity.PREF_CACHE_SIZE_LIST,
                            HumbleViewAPI.DEFAULT_CACHE_SIZE.toString()).toLong())
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
        // HumbleViewAPI.debug = true
    }
}
