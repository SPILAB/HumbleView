package spilab.net.humbleimageview.features.transition

internal abstract class TransitionDrawable : Transition {

    private var handleDrawableReplaced = true

    fun updateDrawable(fromBlock: () -> Unit) {
        handleDrawableReplaced = false
        fromBlock()
        handleDrawableReplaced = true
    }

    override fun drawableReplaced() {
        if (handleDrawableReplaced) {
            cancel()
        }
    }

    abstract fun cancel()
}