package spilab.net.humbleimageview.view

import java.lang.IllegalArgumentException

enum class DebugViewFlags(val value: Int, private val layoutName: String) {
    DECODE_SIZE(1, "decodeSize"),
    REQUEST_VIEW_SIZE(1 shl 1, "requestViewSize");

    companion object {
        fun fromLayoutName(name: String): DebugViewFlags {
            for (value in DebugViewFlags.values()) {
                if (value.layoutName == name) return value
            }
            throw IllegalArgumentException("Unknown debug flag.")
        }
    }

    fun isEnable(flag: Int): Boolean {
        return ((value and flag) > 0)
    }
}