package spilab.net.humbleimageview.log

import android.util.Log
import spilab.net.humbleimageview.model.HumbleViewConfig

internal class HumbleLogs {

    companion object {

        fun log(msg: String) {
            humbleLog.log(msg)
        }

        private val humbleLog: HumbleLog by lazy {
            if (HumbleViewConfig.log) {
                return@lazy HumbleLogEnable()
            }
            return@lazy HumbleLogDisable()
        }

        private interface HumbleLog {
            fun log(msg: String)
        }

        private class HumbleLogEnable : HumbleLog {

            override fun log(msg: String) {
                Log.d("HumbleView", msg)
            }
        }

        private class HumbleLogDisable : HumbleLog {
            override fun log(msg: String) {}
        }
    }
}