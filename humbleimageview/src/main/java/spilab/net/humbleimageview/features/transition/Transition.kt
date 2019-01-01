package spilab.net.humbleimageview.features.transition

internal interface Transition {

    companion object {
        const val CURRENT_IDX = 0
        const val NEXT_IDX = 1
    }

    fun start()
    fun prepareOnDraw()
    fun onAttached()
    fun onDetached()
    fun drawableReplaced()
}