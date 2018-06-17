package spilab.net.humbleimageview.model

import okhttp3.OkHttpClient


class HumbleViewAPI {

    companion object {

        var debug = false
        var log = false

        val okHttpClient: OkHttpClient by lazy {
            return@lazy OkHttpClient()
        }
    }
}