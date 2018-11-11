package spilab.net.humbleimageview.android

import android.support.v4.view.ViewCompat
import android.view.View

class AndroidViewCompat {

    fun isAttachedToWindow(view: View): Boolean {
        return ViewCompat.isAttachedToWindow(view)
    }
}