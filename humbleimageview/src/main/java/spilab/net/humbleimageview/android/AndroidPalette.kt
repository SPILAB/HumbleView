package spilab.net.humbleimageview.android

import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v7.graphics.Palette

internal class AndroidPalette {

    fun generate(bitmap: Bitmap, onGenerated: (palette: Palette) -> Unit): AsyncTask<Bitmap, Void, Palette> {
        return Palette.Builder(bitmap).generate {
            onGenerated(it)
        }
    }
}