package spilab.net.humbleimageview.model.cache

import okio.ByteString
import spilab.net.humbleimageview.api.HumbleViewAPI
import java.io.File

class OfflineCache {

    private val cacheDirectory: File

    constructor(directory: File) {
        cacheDirectory = directory
    }

    internal fun put(key: String, data: ByteArray) {
        val file = File(cacheDirectory, getFileName(key))
        file.writeBytes(data)
    }

    internal fun get(key: String): ByteArray? {
        val file = File(cacheDirectory, getFileName(key))
        return if (file.exists()) {
            file.readBytes()
        } else {
            null
        }
    }

    private inline fun getFileName(key: String): String {
        return ByteString.encodeUtf8(key).md5().hex()
    }
}