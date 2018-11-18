package spilab.net.humbleimageview.android

import androidx.core.view.ViewCompat
import android.view.View

class AndroidViewCompat {

    fun isAttachedToWindow(view: View): Boolean {
        return ViewCompat.isAttachedToWindow(view)
    }
}