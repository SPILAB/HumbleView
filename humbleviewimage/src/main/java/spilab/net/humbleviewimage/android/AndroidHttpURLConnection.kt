package spilab.net.humbleviewimage.android

import java.net.HttpURLConnection
import java.net.URL

class AndroidHttpURLConnection {

    fun openConnection(url: String): HttpURLConnection {
        val uri = URL(url)
        return uri.openConnection() as HttpURLConnection
    }
}