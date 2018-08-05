package spilab.net.humbleimageview.features.transition

interface Transition {

    companion object {
        const val CURRENT_IDX = 0
        const val NEXT_IDX = 1
    }

    interface TransitionListener {
        fun onTransitionCompleted()
    }

    fun prepareOnDraw()
    fun cancel()
}