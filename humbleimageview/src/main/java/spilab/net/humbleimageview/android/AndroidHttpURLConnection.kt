package spilab.net.humbleimageview.android

import java.net.HttpURLConnection
import java.net.URL

internal class AndroidHttpURLConnection {

    fun openConnection(url: String): HttpURLConnection {
        val uri = URL(url)
        return uri.openConnection() as HttpURLConnection
    }
}