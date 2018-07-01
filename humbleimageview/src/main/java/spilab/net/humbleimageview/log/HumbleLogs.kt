package spilab.net.humbleimageview.log

import android.util.Log

internal class HumbleLogs {

    companion object {

        var enable: Boolean = false

        fun log(msg: String, vararg args: Any?) {
            humbleLog.log(String.format(msg, *args))
        }

        private val humbleLog: HumbleLog by lazy {
            if (enable) {
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