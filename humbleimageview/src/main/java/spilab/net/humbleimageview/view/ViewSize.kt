package spilab.net.humbleimageview.view

data class ViewSize(val width: Int = 0, val height: Int = 0) {

    override fun toString(): String {
        return "(${width}x$height)"
    }
}

