package spilab.net.humbleimageview.model

internal data class ViewSize(val width: Int = 0, val height: Int = 0) {

    fun isValid(): Boolean = (width != 0 && height != 0)
}